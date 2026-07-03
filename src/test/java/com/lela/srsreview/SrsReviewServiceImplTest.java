package com.lela.srsreview;

import com.lela.cardprogress.CardProgressRepository;
import com.lela.cardprogress.domain.CardProgress;
import com.lela.cardprogress.domain.ReviewableCardState;
import com.lela.flashcard.FlashcardRepository;
import com.lela.flashcard.domain.Flashcard;
import com.lela.reviewsession.ReviewSessionRepository;
import com.lela.reviewsession.domain.ReviewSession;
import com.lela.srsreview.domain.SrsReview;
import com.lela.srsreview.dto.SrsReviewRequest;
import com.lela.srsreview.dto.SrsReviewResponse;
import com.lela.users.UsersRepository;
import com.lela.users.domain.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
public class SrsReviewServiceImplTest {

    @Mock
    private SrsReviewRepository srsReviewRepository;

    @Mock
    private ReviewSessionRepository reviewSessionRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private FlashcardRepository flashcardRepository;

    @Mock
    private CardProgressRepository cardProgressRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SrsReviewServiceImpl service;

    private SrsReview entity;

    @BeforeEach
    void setUp() {
        Users user = new Users();
        user.setId(1L);

        Flashcard card = new Flashcard();
        card.setId(1L);

        ReviewSession session = new ReviewSession();
        session.setId(1L);

        entity = new SrsReview();
        entity.setId(1L);
        entity.setUser(user);
        entity.setCard(card);
        entity.setReviewSession(session);
    }

    @Test
    void reviewCard_Success() {
        SrsReviewRequest request = new SrsReviewRequest();
        request.setClientEventId("evt123");
        request.setReviewSessionId(1L);
        request.setUserId(1L);
        request.setCardId(1L);
        request.setRating(4); // Easy
        request.setNewState(ReviewableCardState.REVIEW);

        ReviewSession session = new ReviewSession();
        session.setId(1L);

        Users user = new Users();
        user.setId(1L);

        Flashcard card = new Flashcard();
        card.setId(1L);

        when(srsReviewRepository.existsByClientEventId("evt123")).thenReturn(false);
        when(reviewSessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(flashcardRepository.findById(1L)).thenReturn(Optional.of(card));

        CardProgress progress = new CardProgress();
        progress.setTotalReviews(0);
        progress.setEasyCount(0);
        progress.setCorrectCount(0);
        
        when(cardProgressRepository.findByUserIdAndCardId(1L, 1L)).thenReturn(Optional.of(progress));
        when(srsReviewRepository.save(any(SrsReview.class))).thenReturn(entity);
        
        SrsReviewResponse response = new SrsReviewResponse();
        when(modelMapper.map(any(SrsReview.class), eq(SrsReviewResponse.class))).thenReturn(response);

        SrsReviewResponse result = service.reviewCard(request);

        assertNotNull(result);
        verify(cardProgressRepository).save(progress);
        verify(srsReviewRepository).save(any(SrsReview.class));
        assertEquals(1, progress.getTotalReviews());
        assertEquals(1, progress.getEasyCount());
    }

    @Test
    void getReviewHistory_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SrsReview> page = new PageImpl<>(Arrays.asList(entity));

        when(srsReviewRepository.findAll(pageable)).thenReturn(page);
        
        SrsReviewResponse response = new SrsReviewResponse();
        when(modelMapper.map(entity, SrsReviewResponse.class)).thenReturn(response);

        Page<SrsReviewResponse> result = service.getReviewHistory(1L, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getReviewStatistics_Success() {
        when(srsReviewRepository.countReviewsInPeriod(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(5L).thenReturn(20L);
        
        Map<String, Long> result = (Map<String, Long>) service.getReviewStatistics(1L);
        
        assertEquals(5L, result.get("todayReviews"));
    }
}
