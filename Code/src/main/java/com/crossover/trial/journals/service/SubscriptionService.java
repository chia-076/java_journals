package com.crossover.trial.journals.service;

import com.crossover.trial.journals.model.User;

import java.util.Collection;

/**
 * Created by YongSengChia on 12/21/2016.
 */
public interface SubscriptionService {
    Collection<User> findSubscribersByCategory(Long categoryId);
}
