package com.hescha.minijira.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table
@Entity
public class Comment extends AbstractEntity {
    @ManyToOne
    private Issue issue;
    @ManyToOne
    private User owner;
    private String text;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateCreated;

    @Override
    public String toString() {
        return owner.getUsername() + " (" + dateCreated + "): " + text;
    }
}
