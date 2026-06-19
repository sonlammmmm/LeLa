package com.lela.feature.users;

import com.lela.domain.enums.UserStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UsersPatchRequest {
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private String avatarUrl;
    private Long nativeLanguageId;
    private Long targetLanguageId;
    private UserStatus status;
    private String timezone;
    private Integer dailyGoalCards;
    private Long xpTotal;
    private Integer streakCurrent;
    private Integer streakLongest;
    private LocalDate lastActivityDate;
    private LocalDateTime lastActiveAt;
    private LocalDateTime emailVerifiedAt;
    private LocalDateTime deletedAt;
}
