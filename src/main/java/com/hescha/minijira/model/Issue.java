package com.hescha.minijira.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Table
@Entity
public class Issue extends AbstractEntity {
    private String name;
    private String description;
    @ManyToOne
    private Column column;
    @ManyToMany
    private List<Label> labels = new ArrayList<>();
    @OneToMany
    private List<Comment> comments = new ArrayList<>();
    @ManyToOne
    private User assigned;
    @ManyToOne
    private User created;
    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activities = new ArrayList<>();
    @ManyToOne
    private Project project;
    private Integer timeSpend;
    private LocalDateTime dateCreated;
    private IssueStatus status = IssueStatus.CREATED;
}
