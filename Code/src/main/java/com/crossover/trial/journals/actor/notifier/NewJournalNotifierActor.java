package com.crossover.trial.journals.actor.notifier;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.crossover.trial.journals.actor.message.NewJournalAddedMessage;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.service.EmailService;
import com.crossover.trial.journals.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Collection;

/**
 * Created by YongSengChia on 12/21/2016.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NewJournalNotifierActor extends UntypedActor {
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    public static final String NAME = "newJournalNotifierActor";

    @Autowired
    EmailService emailService;

    @Autowired
    SubscriptionService subscriptionService;

    @Override
    public void preStart() throws Exception {
        logger.info("Actor [{}] started.", NewJournalNotifierActor.class.getSimpleName());
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof NewJournalAddedMessage) {
            NewJournalAddedMessage message = (NewJournalAddedMessage) msg;
            Journal journal = message.getJournal();
            logger.info("New Journal added {}", journal.toString());

            Collection<User> recipients = message.getRecipients();
            recipients.forEach(recipient -> {
                try {
                    logger.info("Sending email notification to [{}, {}]", recipient.getLoginName(), recipient.getEmail());
                    emailService.sendPlainText(recipient.getEmail(), getPlainText(journal));
                    Thread.sleep(1000);
                } catch (MessagingException | InterruptedException e) {
                    logger.info("Failed to send email {} notification", recipient.getEmail());
                }
            });

        } else {
            unhandled(msg);
        }
    }

    private String getPlainText(Journal journal) {
        String plainText =
                "\nNew journal added." +
                "\nName: " + journal.getName() +
                "\nDescription: " + journal.toString();

        return plainText;
    }
}
