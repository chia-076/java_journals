package com.crossover.trial.journals.actor.notifier;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.crossover.trial.journals.actor.message.ScheduledJournalNotificationMessage;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.service.EmailService;
import com.crossover.trial.journals.service.JournalService;
import com.crossover.trial.journals.service.UserService;
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
public class ScheduledJournalNotifierActor extends UntypedActor {
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    JournalService journalService;

    @Override
    public void preStart() throws Exception {
        logger.info("Actor [{}] started.", ScheduledJournalNotifierActor.class.getSimpleName());
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof ScheduledJournalNotificationMessage) {
            logger.info("Executing scheduled journal notification ...");

            Collection<Journal> latestJournals = journalService.getLatestJournals();
            String plainText = getPlainText(latestJournals);
            Collection<User> recipients = userService.findAll();
            recipients.forEach(recipient -> {
                try {
                    logger.info("Sending scheduled email notification to [{}, {}]",
                            recipient.getLoginName(), recipient.getEmail());
                    emailService.sendPlainText(recipient.getEmail(), plainText);
                    Thread.sleep(1000);
                } catch (MessagingException | InterruptedException e) {
                    logger.info("Failed to send scheduled email notification to [{}, {}]",
                            recipient.getLoginName(), recipient.getEmail());
                }
            });

        } else {
            unhandled(msg);
        }
    }

    private String getPlainText(Collection<Journal> journals) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n(Scheduled) New journals added.");
        builder.append("\n");

        journals.forEach(journal -> {
            builder.append("\nName: ");
            builder.append(journal.getName());
            builder.append("\nDescription: ");
            builder.append(journal.toString());
            builder.append("\n");
        });

        return builder.toString();
    }
}
