package com.lela.domain.enums;

// Enum trạng thái xử lý bản ghi idempotency.
public enum IdempotencyProcessingStatus {
    PROCESSING, // Request đang được xử lý.
    COMPLETED, // Request đã xử lý xong và có phản hồi lưu lại.
    FAILED // Request xử lý thất bại.
}
