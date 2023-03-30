package com.hescha.minijira.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Table
@Entity
public class Column extends AbstractEntity {
    private String name;
    @ManyToOne
    private Project project;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "column")
    private List<Issue> issues = new ArrayList<>();

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
