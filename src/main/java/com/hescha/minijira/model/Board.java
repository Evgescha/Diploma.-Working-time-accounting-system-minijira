package com.hescha.minijira.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Table
@Entity
public class Board extends AbstractEntity {
    @OneToOne
    private Project project;
    @OneToMany
    private List<Column> columns = new ArrayList<>();
    @OneToMany
    private List<Issue> issues = new ArrayList<>();
    @OneToMany
    private List<Label> labels = new ArrayList<>();
}
