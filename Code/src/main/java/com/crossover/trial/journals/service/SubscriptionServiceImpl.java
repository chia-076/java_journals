package com.crossover.trial.journals.service;

import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.SubscriptionRepository;
import com.crossover.trial.journals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.*;

/**
 * Created by YongSengChia on 12/21/2016.
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Collection<User> findSubscribersByCategory(Long categoryId) {
        Collection<Subscription> subscriptions = subscriptionRepository.findByCategoryId(categoryId);
        List<Long> userIds = new ArrayList<>();
        subscriptions.forEach(subscription -> userIds.add(subscription.getUser().getId()));
        List<User> retrievedSubscriber = userRepository.findAll(userIds);
        if(retrievedSubscriber == null) {
            retrievedSubscriber = emptyList();
        }
        return retrievedSubscriber;
    }
}
