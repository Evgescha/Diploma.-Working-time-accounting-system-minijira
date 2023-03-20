package com.hescha.minijira.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Table
@Entity
public class Label extends AbstractEntity {
    private String name;
    private String description;
    @ManyToMany
    private List<Issue> issues = new ArrayList<>();
}
