package com.crossover.trial.journals.repository;

import com.crossover.trial.journals.model.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Created by YongSengChia on 12/21/2016.
 */
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    Collection<Subscription> findByCategoryId(Long categoryId);
}
