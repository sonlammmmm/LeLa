package com.lela.feature.userroleassignment;

import com.lela.common.exception.NotFoundExeception;
import com.lela.feature.role.Role;
import com.lela.feature.role.RoleRepository;
import com.lela.feature.users.Users;
import com.lela.feature.users.UsersRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleAssignmentServiceImpl implements UserRoleAssignmentService {
    private final UserRoleAssignmentRepository repository;
    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UserRoleAssignmentResponse> findByUserId(Long userId) {
        // Tìm tất cả các phân quyền và lọc theo mã định danh người dùng
        return repository.findAll().stream()
                .filter(u -> u.getId().getUserId().equals(userId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserRoleAssignmentResponse assignRole(UserRoleAssignmentCreateRequest request, Long assignedByUserId) {
        // Kiểm tra sự tồn tại của người dùng được gán vai trò
        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundExeception("User not found"));

        // Kiểm tra sự tồn tại của vai trò cần gán
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new NotFoundExeception("Role not found"));

        // Kiểm tra thông tin người thực hiện gán vai trò
        Users assignedBy = assignedByUserId != null ? usersRepository.findById(assignedByUserId).orElse(null) : null;

        // Khởi tạo khóa chính composite và thực thể phân quyền
        UserRoleAssignmentId id = new UserRoleAssignmentId(request.getUserId(), request.getRoleId());
        UserRoleAssignment entity = UserRoleAssignment.builder()
                .id(id)
                .user(user)
                .role(role)
                .assignedBy(assignedBy)
                .build();
        
        entity = repository.save(entity);
        return mapToResponse(entity);
    }

    @Override
    public void unassignRole(Long userId, Long roleId) {
        // Tạo khóa chính composite và tiến hành xóa bản ghi phân quyền
        UserRoleAssignmentId id = new UserRoleAssignmentId(userId, roleId);
        repository.deleteById(id);
    }

    private UserRoleAssignmentResponse mapToResponse(UserRoleAssignment entity) {
        // Sử dụng ModelMapper để tự động ánh xạ thực thể sang DTO phản hồi chứa các sub-DTO
        return modelMapper.map(entity, UserRoleAssignmentResponse.class);
    }
}
