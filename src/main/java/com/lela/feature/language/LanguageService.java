package com.lela.feature.language;

import java.util.List;
import java.util.Optional;

public interface LanguageService {
    // Lấy danh sách toàn bộ ngôn ngữ.
    List<LanguageResponse> findAll();

    // Tìm kiếm ngôn ngữ theo ID.
    Optional<LanguageResponse> findById(Long id);

    // Tạo mới ngôn ngữ.
    LanguageResponse create(LanguageCreateRequest request);

    // Cập nhật một phần thông tin ngôn ngữ (PATCH).
    LanguageResponse patch(Long id, LanguagePatchRequest request);

    // Xóa ngôn ngữ theo ID.
    void deleteById(Long id);
}
