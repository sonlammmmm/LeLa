package com.lela.admin;

import com.lela.common.ApiResponse;
import com.lela.deck.DeckRepository;
import com.lela.flashcard.FlashcardRepository;
import com.lela.payment.PaymentRepository;
import com.lela.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// ponytail: Thin aggregation controller — just counts. No service layer needed until
// we add time-series queries or caching.
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminMetricsController {

    private final UsersRepository usersRepository;
    private final DeckRepository deckRepository;
    private final FlashcardRepository flashcardRepository;
    private final PaymentRepository paymentRepository;

    @GetMapping("/metrics")
    public ApiResponse<Map<String, Object>> getMetrics() {
        long totalUsers = usersRepository.count();
        long systemDecks = deckRepository.count();
        long totalFlashcards = flashcardRepository.count();
        long totalPayments = paymentRepository.count();

        // ponytail: monthlyRevenue and charts need real time-series queries.
        // For now, return counts only. Frontend handles nulls gracefully.
        Map<String, Object> metrics = Map.of(
            "totalUsers", totalUsers,
            "systemDecks", systemDecks,
            "totalFlashcards", totalFlashcards,
            "totalPayments", totalPayments,
            "monthlyRevenue", 0,
            "userActivity", List.of(),
            "subscriptionDistribution", List.of()
        );

        return ApiResponse.success(metrics, "Dashboard metrics");
    }
}
