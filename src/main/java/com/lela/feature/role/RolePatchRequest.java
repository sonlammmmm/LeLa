package com.lela.feature.role;

import lombok.Data;

@Data
public class RolePatchRequest {
    private String roleCode;
    private String name;
    private String description;
    private Boolean isActive;
}
