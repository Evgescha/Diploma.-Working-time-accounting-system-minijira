package com.hescha.minijira.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table
@Entity
public class Activity extends AbstractEntity {
    @OneToOne
    private User owner;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "issue_id")
    private Issue issue;
    private ActivityType type;
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Override
    public String toString() {
        return description;
    }
}
