package com.crossover.trial.journals.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.crossover.trial.journals.actor.message.AkkaMessage;
import com.crossover.trial.journals.actor.message.ScheduledJournalNotificationMessage;
import com.crossover.trial.journals.actor.spring.SpringExtension;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by YongSengChia on 12/21/2016.
 */
@Component
public class AkkaHelper {

    private final static Logger log = Logger.getLogger(AkkaHelper.class);

    @Autowired
    private ActorSystem actorSystem;

    @Autowired
    private SpringExtension springExtension;

    @PostConstruct
    public ActorRef scheduledJournalNotifierActor() {
        ActorRef actorRef = actorSystem.actorOf(
                springExtension.props("scheduledJournalNotifierActor"), "scheduled-journal-notifier-actor");
        actorSystem
                .scheduler()
                .schedule(
                        Duration.create(0, TimeUnit.MILLISECONDS),
                        Duration.create(5, TimeUnit.MINUTES),
                        actorRef, new ScheduledJournalNotificationMessage(),
                        actorSystem.dispatcher(), null);
        return actorRef;
    }

    public void tell(String actorName, Collection<AkkaMessage> messages) {
        ActorRef actorRef = actorSystem.actorOf(springExtension.props(actorName));
        messages.forEach(message -> actorRef.tell(message, null));
    }
}
