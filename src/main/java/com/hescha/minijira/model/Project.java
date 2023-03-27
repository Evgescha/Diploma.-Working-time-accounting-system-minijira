package com.hescha.minijira.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
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
public class Project extends AbstractEntity {
    private String name;
    @javax.persistence.Column(length = 1000)
    private String description;
    private String image;
    private ProjectStatusType status = ProjectStatusType.ALIVE;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    private List<User> members = new ArrayList<>();
    @ManyToOne
    private User owner;
    @OneToMany
    private List<Column> columns = new ArrayList<>();
    @OneToMany
    private List<Issue> issues = new ArrayList<>();
    @OneToMany
    private List<Label> labels = new ArrayList<>();
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime dateCreated;

    @Override
    public String toString() {
        return "Project{" +
                "id=" + getId() +
                ", name='" + (name == null ? "" : name) + '\'' +
                ", description='" + (description == null ? "" : description) + '\'' +
                ", image='" + (image == null ? "" : image) + '\'' +
                ", status=" + status +
                ", members=" + (members.size()==0 ? "" : members) +
                ", owner=" + (owner == null ? "" : owner) +
                ", dateCreated=" + (dateCreated == null ? "" : dateCreated) +
                '}';
    }
}
