package com.crossover.trial.journals.actor.spring;

import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by YongSengChia on 12/21/2016.
 * Integrating Akka into Spring
 */
@Component
public class SpringExtension implements Extension {

    private ApplicationContext applicationContext;

    public void initialize(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Props props(String actorBeanName) {
        return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
    }
}
