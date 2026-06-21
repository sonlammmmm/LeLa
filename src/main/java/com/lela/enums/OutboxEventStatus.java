package com.lela.enums;

// Enum trạng thái xử lý sự kiện outbox.
public enum OutboxEventStatus {
    PENDING, // Sự kiện chờ xử lý.
    PROCESSING, // Sự kiện đang được xử lý.
    PROCESSED, // Sự kiện đã xử lý thành công.
    FAILED // Sự kiện xử lý thất bại.
}
