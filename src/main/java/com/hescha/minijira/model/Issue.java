package com.hescha.minijira.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Table
@Entity
public class Issue extends AbstractEntity {
    @ManyToOne
    private Board board;
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
    @ManyToMany
    private List<Issue> related = new ArrayList<>();
    @OneToMany
    private List<Activity> activities = new ArrayList<>();
    private Integer timeSpend;
    private LocalDateTime dateCreated;
    private IssueStatus status = IssueStatus.CREATED;
}
