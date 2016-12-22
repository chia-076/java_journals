package com.crossover.trial.journals.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

@Entity
public class Journal {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Date publishDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Column(nullable = false)
    private String uuid; //external id

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    public Journal() {
    }

    public Journal(Long id, String name, Date publishDate, Publisher publisher, String uuid, Category category) {
        this.id = id;
        this.name = name;
        this.publishDate = publishDate;
        this.publisher = publisher;
        this.uuid = uuid;
        this.category = category;
    }

    @PrePersist
    void onPersist() {
        this.publishDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uuid", uuid)
                .append("id", id)
                .append("name", name)
                .append("publishedDate", publishDate)
                .append("publisher", publisher)
                .append("category", category)
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(uuid)
                .append(id)
                .append(name)
                .append(publishDate)
                .append(publisher)
                .append(category)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Journal)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        final Journal that = (Journal) obj;

        return new EqualsBuilder()
                .append(this.uuid, that.uuid)
                .append(this.id, that.id)
                .append(this.name, that.name)
                .append(this.publishDate, that.publishDate)
                .append(this.publisher, that.publisher)
                .append(this.category, that.category)
                .isEquals();
    }
}
