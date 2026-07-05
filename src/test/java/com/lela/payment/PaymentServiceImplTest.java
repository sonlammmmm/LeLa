package com.lela.payment;

import com.lela.payment.domain.Payment;
import com.lela.payment.domain.PaymentStatus;
import com.lela.payment.dto.PaymentRequest;
import com.lela.payment.dto.PaymentResponse;
import com.lela.users.domain.Users;
import com.lela.usersubscription.domain.UserSubscription;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository repository;

    @Mock
    private com.lela.users.UsersRepository usersRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PaymentServiceImpl service;

    private Payment entity;

    @BeforeEach
    void setUp() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Mockito.lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.lenient().when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        Users user = new Users();
        user.setId(1L);
        Mockito.lenient().when(usersRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserSubscription sub = new UserSubscription();
        sub.setId(2L);

        entity = new Payment();
        entity.setId(1L);
        entity.setUser(user);
        entity.setSubscription(sub);
        entity.setAmount(new BigDecimal("100.00"));
        entity.setStatus(PaymentStatus.SUCCEEDED);
    }

    @Test
    void getAll_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Payment> page = new PageImpl<>(Arrays.asList(entity));

        when(repository.findAll(pageable)).thenReturn(page);

        Page<PaymentResponse> result = service.getAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());
    }

    @Test
    void getById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        PaymentResponse result = service.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void create_Success() {
        PaymentRequest request = new PaymentRequest();
        request.setSubscriptionId(2L);
        request.setAmount(new BigDecimal("100.00"));

        when(entityManager.getReference(Users.class, 1L)).thenReturn(new Users());
        when(entityManager.getReference(UserSubscription.class, 2L)).thenReturn(new UserSubscription());
        when(repository.save(any(Payment.class))).thenReturn(entity);

        PaymentResponse result = service.create(request);

        assertNotNull(result);
        verify(repository).save(any(Payment.class));
    }

    @Test
    void update_Success() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal("200.00"));
        request.setStatus(PaymentStatus.REFUNDED);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        PaymentResponse result = service.update(1L, request);

        assertNotNull(result);
        assertEquals(PaymentStatus.REFUNDED, entity.getStatus());
        verify(repository).save(entity);
    }

    @Test
    void delete_Success() {
        when(repository.existsById(1L)).thenReturn(true);
        service.delete(1L);
        verify(repository).deleteById(1L);
    }
}
