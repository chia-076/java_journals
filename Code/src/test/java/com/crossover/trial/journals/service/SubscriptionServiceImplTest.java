package com.crossover.trial.journals.service;

import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.SubscriptionRepository;
import com.crossover.trial.journals.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.thymeleaf.util.DateUtils;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by YongSengChia on 12/22/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class SubscriptionServiceImplTest {
    @InjectMocks
    SubscriptionServiceImpl subscriptionService;

    @Mock
    SubscriptionRepository subscriptionRepository;

    @Mock
    UserRepository userRepository;

    @Test
    public void findSubscribersByCategory_whenSubscriberFound() throws Exception {
        User expectedSubscriber = new User();
        Category category = new Category(9L, "Testing");
        Subscription subscription = new Subscription(1L, expectedSubscriber, DateUtils.createNow().getTime(), category);

        when(subscriptionRepository.findByCategoryId(category.getId())).thenReturn(asList(subscription));
        when(userRepository.findAll(asList(expectedSubscriber.getId()))).thenReturn(asList(expectedSubscriber));
        List<User> retrievedSubscriber = (List<User>) subscriptionService.findSubscribersByCategory(category.getId());

        assertEquals(retrievedSubscriber.size(), 1);
        assertEquals(retrievedSubscriber.get(0), expectedSubscriber);

        verify(subscriptionRepository, times(1)).findByCategoryId(category.getId());
        verify(userRepository, times(1)).findAll(asList(expectedSubscriber.getId()));
    }

    @Test
    public void findSubscribersByCategory_whenNoSubscriberFound() throws Exception {
        Category category = new Category(9L, "Testing");
        when(subscriptionRepository.findByCategoryId(any())).thenReturn(emptyList());
        when(userRepository.findAll(emptyList())).thenReturn(emptyList());
        List<User> retrievedSubscriber = (List<User>) subscriptionService.findSubscribersByCategory(category.getId());

        assertNotNull(retrievedSubscriber);
        assertTrue(retrievedSubscriber.isEmpty());

        verify(subscriptionRepository, times(1)).findByCategoryId(category.getId());
        verify(userRepository, times(1)).findAll(emptyList());
    }

}