package com.lela.srsreview.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewStatsResponse {
    private long todayReviews;
    private long last7DaysReviews;
}