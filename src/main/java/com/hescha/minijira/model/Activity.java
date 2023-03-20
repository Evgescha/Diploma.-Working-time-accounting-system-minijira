package com.hescha.minijira.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table
@Entity
public class Activity extends AbstractEntity {
    @ManyToOne
    private Issue issue;
    private ActivityType type;
    private String description;
    private LocalDateTime dateCreated;
}
