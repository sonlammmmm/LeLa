package com.lela.domain.entity;

import com.lela.domain.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "languages")
public class Language extends AuditableEntity {

    @Column(name = "lang_code", nullable = false, unique = true, length = 10)
    private String langCode;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "native_name", length = 100)
    private String nativeName;

    @Column(name = "flag_url", length = 255)
    private String flagUrl;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}