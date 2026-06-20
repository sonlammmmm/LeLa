package com.lela.srsreview.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewEventDto {

    @NotNull(message = "Card ID không được để trống")
    private Long cardId;

    @NotBlank(message = "Client Event ID không được để trống")
    @Size(max = 36, message = "Client Event ID phải là chuỗi UUID hợp lệ")
    private String clientEventId;

    @NotNull(message = "Đánh giá chất lượng (Rating) không được để trống")
    @Min(value = 1, message = "Rating nhỏ nhất là 1 (AGAIN)")
    @Max(value = 4, message = "Rating lớn nhất là 4 (EASY)")
    private Integer rating;

    @NotNull(message = "Thời gian phản hồi không được để trống")
    @PositiveOrZero(message = "Thời gian phản hồi phản xạ phải từ 0ms trở lên")
    private Integer responseMs;

    @NotNull(message = "Thời điểm ôn tập ở client không được để trống")
    private LocalDateTime clientReviewedAt;
}