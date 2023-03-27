package com.hescha.minijira.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Table
@Entity
public class Label extends AbstractEntity {
    private String name;
    private String description;
    @ManyToOne
    private Project project;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Issue> issues = new ArrayList<>();
}
