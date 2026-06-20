package com.lela.auth;

import com.lela.common.ApiResponse;
import com.lela.common.exception.BadRequestException;
import com.lela.common.exception.NotFoundExeception;
import com.lela.role.Role;
import com.lela.role.RoleRepository;
import com.lela.auth.dto.AuthResponse;
import com.lela.auth.dto.LoginRequest;
import com.lela.auth.dto.RefreshTokenRequest;
import com.lela.auth.dto.RegisterRequest;
import com.lela.userroleassignment.UserRoleAssignment;
import com.lela.userroleassignment.UserRoleAssignmentRepository;
import com.lela.userroleassignment.dto.UserRoleAssignmentId;
import com.lela.users.Users;
import com.lela.users.UsersRepository;
import com.lela.users.UserStatus;
import com.lela.refreshtokensession.RefreshTokenSession;
import com.lela.refreshtokensession.RefreshTokenSessionRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Các API xác thực: đăng ký, đăng nhập, đăng xuất, và làm mới token")
@SuppressWarnings("null")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final UserRoleAssignmentRepository userRoleAssignmentRepository;
    private final RefreshTokenSessionRepository refreshTokenSessionRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Transactional
    @Operation(summary = "Đăng ký tài khoản", description = "Tạo tài khoản người dùng mới và gán vai trò mặc định LEARNER.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Đăng ký thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ hoặc tên đăng nhập/email đã được sử dụng")
    })
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid RegisterRequest request) {
        if (usersRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Tên đăng nhập đã được sử dụng");
        }
        if (usersRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email đã được đăng ký");
        }

        Users user = Users.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .status(UserStatus.ACTIVE)
                .timezone("Asia/Ho_Chi_Minh")
                .dailyGoalCards(10)
                .xpTotal(0L)
                .streakCurrent(0)
                .streakLongest(0)
                .build();

        user = usersRepository.save(user);

        Role role = roleRepository.findByRoleCode("LEARNER")
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy vai trò mặc định LEARNER"));

        UserRoleAssignmentId assignmentId = new UserRoleAssignmentId(user.getId(), role.getId());
        UserRoleAssignment assignment = UserRoleAssignment.builder()
                .id(assignmentId)
                .user(user)
                .role(role)
                .build();

        userRoleAssignmentRepository.save(assignment);

        return ResponseEntity.ok(ApiResponse.successMessage("Đăng ký thành công"));
    }

    @PostMapping("/login")
    @Transactional
    @Operation(summary = "Đăng nhập người dùng", description = "Xác thực tài khoản và trả về cặp access token, refresh token.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Đăng nhập thành công",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Tên đăng nhập/email hoặc mật khẩu không chính xác")
    })
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
        );

        Users user = usersRepository.findByUsername(loginRequest.getUsernameOrEmail())
                .or(() -> usersRepository.findByEmail(loginRequest.getUsernameOrEmail()))
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy người dùng"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        String tokenHash = hashToken(refreshToken);
        Instant expiresInstant = jwtService.extractExpiration(refreshToken);
        LocalDateTime expiresAt = expiresInstant != null
                ? LocalDateTime.ofInstant(expiresInstant, ZoneId.systemDefault())
                : LocalDateTime.now().plusDays(7);

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        RefreshTokenSession session = RefreshTokenSession.builder()
                .user(user)
                .tokenHash(tokenHash)
                .tokenFamilyId(UUID.randomUUID().toString())
                .deviceName(request.getHeader("User-Agent"))
                .ipAddress(ip)
                .userAgent(request.getHeader("User-Agent"))
                .expiresAt(expiresAt)
                .lastUsedAt(LocalDateTime.now())
                .build();

        refreshTokenSessionRepository.save(session);

        AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(user.getRoleCodes())
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userInfo)
                .build();

        return ResponseEntity.ok(ApiResponse.success(authResponse, "Đăng nhập thành công"));
    }

    @PostMapping("/refresh-token")
    @Transactional
    @Operation(summary = "Làm mới access token", description = "Cấp access token mới khi refresh token còn hiệu lực.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Làm mới token thành công",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Refresh token không hợp lệ, hết hạn hoặc đã bị thu hồi")
    })
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody @Valid RefreshTokenRequest refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();
        if (refreshToken == null || !jwtService.isRefreshToken(refreshToken)) {
            throw new BadRequestException("Định dạng refresh token không hợp lệ");
        }

        String tokenHash = hashToken(refreshToken);
        RefreshTokenSession session = refreshTokenSessionRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy phiên làm việc của refresh token"));

        if (session.getRevokedAt() != null) {
            throw new BadRequestException("Refresh token đã bị thu hồi");
        }
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Refresh token đã hết hạn");
        }

        Users user = session.getUser();
        String newAccessToken = jwtService.generateAccessToken(user);

        session.setLastUsedAt(LocalDateTime.now());
        refreshTokenSessionRepository.save(session);

        AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(user.getRoleCodes())
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .user(userInfo)
                .build();

        return ResponseEntity.ok(ApiResponse.success(authResponse, "Làm mới token thành công"));
    }

    @PostMapping("/logout")
    @Transactional
    @Operation(summary = "Đăng xuất", description = "Hủy phiên làm việc của refresh token để chặn yêu cầu cấp lại token mới.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Đăng xuất thành công")
    })
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody @Valid RefreshTokenRequest logoutRequest) {
        String refreshToken = logoutRequest.getRefreshToken();
        String tokenHash = hashToken(refreshToken);
        Optional<RefreshTokenSession> sessionOpt = refreshTokenSessionRepository.findByTokenHash(tokenHash);

        if (sessionOpt.isPresent()) {
            RefreshTokenSession session = sessionOpt.get();
            session.setRevokedAt(LocalDateTime.now());
            session.setRevokeReason("Đã đăng xuất");
            refreshTokenSessionRepository.save(session);
        }

        return ResponseEntity.ok(ApiResponse.successMessage("Đăng xuất thành công"));
    }

    @GetMapping("/profile")
    @Transactional(readOnly = true)
    @Operation(summary = "Lấy thông tin cá nhân", description = "Lấy thông tin tài khoản đang đăng nhập hiện tại (yêu cầu access token hợp lệ).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lấy thông tin cá nhân thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Chưa xác thực - access token không hợp lệ hoặc bị thiếu")
    })
    public ResponseEntity<ApiResponse<AuthResponse.UserInfo>> getProfile(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName();
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy người dùng"));
        AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(user.getRoleCodes())
                .build();
        return ResponseEntity.ok(ApiResponse.success(userInfo, "Lấy thông tin cá nhân thành công"));
    }

    @GetMapping("/admin-only")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API dành riêng cho Admin", description = "Endpoint bị giới hạn quyền truy cập chỉ dành cho tài khoản Admin.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Từ chối truy cập - yêu cầu tài khoản ADMIN")
    })
    public ResponseEntity<ApiResponse<String>> adminOnly() {
        return ResponseEntity.ok(ApiResponse.success("Chào mừng Admin"));
    }

    private String hashToken(String token) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi băm token", e);
        }
    }
}
