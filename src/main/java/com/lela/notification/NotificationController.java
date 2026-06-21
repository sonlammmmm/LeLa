package com.lela.notification;

import com.lela.common.ApiResponse;
import com.lela.notification.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    private static final String MSG_FETCH_ALL_SUCCESS = "Tải danh sách tất cả thông báo thành công.";
    private static final String MSG_FETCH_UNREAD_SUCCESS = "Tải danh sách thông báo chưa đọc thành công.";
    private static final String MSG_MARK_READ_SUCCESS = "Đã đánh dấu đọc thông báo thành công.";
    private static final String MSG_MARK_ALL_READ_SUCCESS = "Đã đánh dấu đọc toàn bộ thông báo thành công.";

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getAll(Pageable pageable) {
        Page<NotificationResponse> data = notificationService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(data, MSG_FETCH_ALL_SUCCESS));
    }

    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getUnread(Pageable pageable) {
        Page<NotificationResponse> data = notificationService.getUnread(pageable);
        return ResponseEntity.ok(ApiResponse.success(data, MSG_FETCH_UNREAD_SUCCESS));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.successMessage(MSG_MARK_READ_SUCCESS));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.ok(ApiResponse.successMessage(MSG_MARK_ALL_READ_SUCCESS));
    }
}