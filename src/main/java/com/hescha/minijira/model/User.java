package com.hescha.minijira.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Table(name = "myUser")
@Entity
public class User extends AbstractEntity {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private String image;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "owner")
    private List<Project> ownProjects = new ArrayList<>();
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<Project> contributeProjects = new HashSet<>();
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Comment> comments = new ArrayList<>();
    private LocalDateTime dateCreated;
}
