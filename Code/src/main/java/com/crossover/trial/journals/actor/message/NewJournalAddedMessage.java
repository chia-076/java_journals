package com.crossover.trial.journals.actor.message;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;

/**
 * Created by YongSengChia on 12/21/2016.
 */
public class NewJournalAddedMessage implements AkkaMessage {
    private Journal journal;
    private Collection<User> recipients;

    public NewJournalAddedMessage(Journal journal, Collection<User> recipients) {
        this.journal = journal;
        this.recipients = recipients;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public Collection<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(Collection<User> recipients) {
        this.recipients = recipients;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(journal)
                .append(recipients)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NewJournalAddedMessage)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        final NewJournalAddedMessage that = (NewJournalAddedMessage) obj;

        return new EqualsBuilder()
                .append(this.journal, that.journal)
                .append(this.recipients, that.recipients)
                .isEquals();
    }
}