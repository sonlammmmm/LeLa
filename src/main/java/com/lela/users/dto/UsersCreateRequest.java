package com.lela.users.dto;

import com.lela.domain.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UsersCreateRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String passwordHash;
    @NotBlank
    private String fullName;
    @NotBlank
    private String avatarUrl;
    @NotNull
    private Long nativeLanguageId;
    @NotNull
    private Long targetLanguageId;
    @NotNull
    private UserStatus status;
    @NotBlank
    private String timezone;
    @NotNull
    private Integer dailyGoalCards;
    @NotNull
    private Long xpTotal;
    @NotNull
    private Integer streakCurrent;
    @NotNull
    private Integer streakLongest;
    @NotNull
    private LocalDate lastActivityDate;
    @NotNull
    private LocalDateTime lastActiveAt;
    @NotNull
    private LocalDateTime emailVerifiedAt;
    @NotNull
    private LocalDateTime deletedAt;
}
