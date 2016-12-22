package com.crossover.trial.journals.config;

import akka.actor.ActorSystem;
import com.crossover.trial.journals.actor.spring.SpringExtension;
import com.crossover.trial.journals.service.UserServiceImpl;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by YongSengChia on 12/21/2016.
 */
@Configuration
public class SpringAkkaConfig {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SpringExtension springExtension;

    @Bean
    public Config akkaConfiguration() {
        return ConfigFactory.load();
    }

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("spring-akka-system", akkaConfiguration());
        springExtension.initialize(applicationContext);
        return system;
    }
}
