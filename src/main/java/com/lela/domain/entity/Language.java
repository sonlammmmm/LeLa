package com.lela.domain.entity;

import com.lela.domain.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "languages")
// Component: Shared reference data - supported learning languages.
public class Language extends AuditableEntity {

    @Column(name = "language_code", nullable = false, unique = true, length = 10)
    private String languageCode; // Mã ngôn ngữ, ví dụ en, ja, vi.

    @Column(nullable = false, length = 100)
    private String name; // Tên ngôn ngữ hiển thị.

    @Column(name = "native_name", length = 100)
    private String nativeName; // Tên bản địa của ngôn ngữ.

    @Column(name = "flag_url", length = 500)
    private String flagUrl; // Đường dẫn biểu tượng cờ.

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // Trạng thái còn được sử dụng.
}
