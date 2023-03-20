package com.hescha.minijira.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Table
@Entity
public class Project extends AbstractEntity {
    private String name;
    private String description;
    private String image;
    private ProjectStatusType status = ProjectStatusType.ALIVE;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    private List<User> members;
    @OneToOne
    private Board board;
    @ManyToOne
    private User owner;
    private LocalDateTime dateCreated;
}
