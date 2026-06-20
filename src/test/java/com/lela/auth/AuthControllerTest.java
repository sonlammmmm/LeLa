package com.lela.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lela.role.Role;
import com.lela.role.RoleRepository;
import com.lela.auth.dto.LoginRequest;
import com.lela.auth.dto.RefreshTokenRequest;
import com.lela.auth.dto.RegisterRequest;
import com.lela.userroleassignment.UserRoleAssignment;
import com.lela.userroleassignment.UserRoleAssignmentRepository;
import com.lela.userroleassignment.dto.UserRoleAssignmentId;
import com.lela.users.Users;
import com.lela.users.UsersRepository;
import com.lela.users.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleAssignmentRepository userRoleAssignmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    private String testUsername;
    private String testEmail;
    private String testPassword;
    private String testFullName;

    @BeforeEach
    public void setUp() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        testUsername = "user_" + suffix;
        testEmail = "email_" + suffix + "@test.com";
        testPassword = "password123";
        testFullName = "Test User " + suffix;
    }

    @Test
    public void testCase1_RegisterSuccess() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username(testUsername)
                .email(testEmail)
                .password(testPassword)
                .fullName(testFullName)
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("Đăng ký thành công")));
    }

    @Test
    public void testCase2_RegisterFail_DuplicateUsernameOrEmail() throws Exception {
        // Pre-save a user
        Users existingUser = Users.builder()
                .username(testUsername)
                .email(testEmail)
                .passwordHash(passwordEncoder.encode(testPassword))
                .fullName(testFullName)
                .status(UserStatus.ACTIVE)
                .timezone("Asia/Ho_Chi_Minh")
                .dailyGoalCards(10)
                .xpTotal(0L)
                .streakCurrent(0)
                .streakLongest(0)
                .build();
        usersRepository.save(existingUser);

        // Try registering with same username
        RegisterRequest requestDupUser = RegisterRequest.builder()
                .username(testUsername)
                .email("another_" + testEmail)
                .password(testPassword)
                .fullName(testFullName)
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDupUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Tên đăng nhập đã được sử dụng")));

        // Try registering with same email
        RegisterRequest requestDupEmail = RegisterRequest.builder()
                .username("another_" + testUsername)
                .email(testEmail)
                .password(testPassword)
                .fullName(testFullName)
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDupEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Email đã được đăng ký")));
    }

    @Test
    public void testCase3_LoginSuccess() throws Exception {
        // Save user
        Users user = Users.builder()
                .username(testUsername)
                .email(testEmail)
                .passwordHash(passwordEncoder.encode(testPassword))
                .fullName(testFullName)
                .status(UserStatus.ACTIVE)
                .timezone("Asia/Ho_Chi_Minh")
                .dailyGoalCards(10)
                .xpTotal(0L)
                .streakCurrent(0)
                .streakLongest(0)
                .build();
        usersRepository.save(user);

        // Map role
        Role role = roleRepository.findByRoleCode("LEARNER").orElseThrow();
        userRoleAssignmentRepository.save(UserRoleAssignment.builder()
                .id(new UserRoleAssignmentId(user.getId(), role.getId()))
                .user(user)
                .role(role)
                .build());
        flushAndClear();

        LoginRequest loginRequest = LoginRequest.builder()
                .usernameOrEmail(testUsername)
                .password(testPassword)
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.accessToken", notNullValue()))
                .andExpect(jsonPath("$.data.refreshToken", notNullValue()))
                .andExpect(jsonPath("$.data.user.username", is(testUsername)));
    }

    @Test
    public void testCase4_LoginFail_WrongPassword() throws Exception {
        // Save user
        Users user = Users.builder()
                .username(testUsername)
                .email(testEmail)
                .passwordHash(passwordEncoder.encode(testPassword))
                .fullName(testFullName)
                .status(UserStatus.ACTIVE)
                .timezone("Asia/Ho_Chi_Minh")
                .dailyGoalCards(10)
                .xpTotal(0L)
                .streakCurrent(0)
                .streakLongest(0)
                .build();
        usersRepository.save(user);

        LoginRequest loginRequest = LoginRequest.builder()
                .usernameOrEmail(testUsername)
                .password("wrongpassword")
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    public void testCase5_RefreshTokenSuccess() throws Exception {
        // Save user
        Users user = Users.builder()
                .username(testUsername)
                .email(testEmail)
                .passwordHash(passwordEncoder.encode(testPassword))
                .fullName(testFullName)
                .status(UserStatus.ACTIVE)
                .timezone("Asia/Ho_Chi_Minh")
                .dailyGoalCards(10)
                .xpTotal(0L)
                .streakCurrent(0)
                .streakLongest(0)
                .build();
        usersRepository.save(user);

        // Map role
        Role role = roleRepository.findByRoleCode("LEARNER").orElseThrow();
        userRoleAssignmentRepository.save(UserRoleAssignment.builder()
                .id(new UserRoleAssignmentId(user.getId(), role.getId()))
                .user(user)
                .role(role)
                .build());
        flushAndClear();

        LoginRequest loginRequest = LoginRequest.builder()
                .usernameOrEmail(testUsername)
                .password(testPassword)
                .build();

        // Perform login to obtain a real refresh token
        String responseBody = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String refreshToken = objectMapper.readTree(responseBody).path("data").path("refreshToken").asText();

        RefreshTokenRequest refreshRequest = RefreshTokenRequest.builder()
                .refreshToken(refreshToken)
                .build();

        mockMvc.perform(post("/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.accessToken", notNullValue()));
    }

    @Test
    public void testCase6_RefreshTokenFail_ExpiredOrInvalid() throws Exception {
        // Invalid format
        RefreshTokenRequest invalidRequest = RefreshTokenRequest.builder()
                .refreshToken("invalid_token_format")
                .build();

        mockMvc.perform(post("/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCase7_CallProtectedApi_Success() throws Exception {
        // Save user
        Users user = Users.builder()
                .username(testUsername)
                .email(testEmail)
                .passwordHash(passwordEncoder.encode(testPassword))
                .fullName(testFullName)
                .status(UserStatus.ACTIVE)
                .timezone("Asia/Ho_Chi_Minh")
                .dailyGoalCards(10)
                .xpTotal(0L)
                .streakCurrent(0)
                .streakLongest(0)
                .build();
        usersRepository.save(user);

        // Map role
        Role role = roleRepository.findByRoleCode("LEARNER").orElseThrow();
        userRoleAssignmentRepository.save(UserRoleAssignment.builder()
                .id(new UserRoleAssignmentId(user.getId(), role.getId()))
                .user(user)
                .role(role)
                .build());
        flushAndClear();

        LoginRequest loginRequest = LoginRequest.builder()
                .usernameOrEmail(testUsername)
                .password(testPassword)
                .build();

        // Login to get access token
        String responseBody = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String accessToken = objectMapper.readTree(responseBody).path("data").path("accessToken").asText();

        // Call profile API with Authorization Header
        mockMvc.perform(get("/auth/profile")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.username", is(testUsername)));
    }

    @Test
    public void testCase8_CallProtectedApi_NoToken() throws Exception {
        // Call profile API without token -> 401 Unauthorized
        mockMvc.perform(get("/auth/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCase9_CallAdminApi_ForbiddenForLearner() throws Exception {
        // Save user
        Users user = Users.builder()
                .username(testUsername)
                .email(testEmail)
                .passwordHash(passwordEncoder.encode(testPassword))
                .fullName(testFullName)
                .status(UserStatus.ACTIVE)
                .timezone("Asia/Ho_Chi_Minh")
                .dailyGoalCards(10)
                .xpTotal(0L)
                .streakCurrent(0)
                .streakLongest(0)
                .build();
        usersRepository.save(user);

        // Map role LEARNER (not ADMIN)
        Role role = roleRepository.findByRoleCode("LEARNER").orElseThrow();
        userRoleAssignmentRepository.save(UserRoleAssignment.builder()
                .id(new UserRoleAssignmentId(user.getId(), role.getId()))
                .user(user)
                .role(role)
                .build());
        flushAndClear();

        LoginRequest loginRequest = LoginRequest.builder()
                .usernameOrEmail(testUsername)
                .password(testPassword)
                .build();

        // Login to get access token
        String responseBody = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String accessToken = objectMapper.readTree(responseBody).path("data").path("accessToken").asText();

        // Call admin only api -> 403 Forbidden
        mockMvc.perform(get("/auth/admin-only")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCase9_CallAdminApi_SuccessForAdmin() throws Exception {
        // Save user
        Users user = Users.builder()
                .username(testUsername)
                .email(testEmail)
                .passwordHash(passwordEncoder.encode(testPassword))
                .fullName(testFullName)
                .status(UserStatus.ACTIVE)
                .timezone("Asia/Ho_Chi_Minh")
                .dailyGoalCards(10)
                .xpTotal(0L)
                .streakCurrent(0)
                .streakLongest(0)
                .build();
        usersRepository.save(user);

        // Map role ADMIN
        Role role = roleRepository.findByRoleCode("ADMIN").orElseThrow();
        userRoleAssignmentRepository.save(UserRoleAssignment.builder()
                .id(new UserRoleAssignmentId(user.getId(), role.getId()))
                .user(user)
                .role(role)
                .build());
        flushAndClear();

        LoginRequest loginRequest = LoginRequest.builder()
                .usernameOrEmail(testUsername)
                .password(testPassword)
                .build();

        // Login to get access token
        String responseBody = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String accessToken = objectMapper.readTree(responseBody).path("data").path("accessToken").asText();

        // Call admin only api -> 200 OK
        mockMvc.perform(get("/auth/admin-only")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", is("Chào mừng Admin")));
    }
}
