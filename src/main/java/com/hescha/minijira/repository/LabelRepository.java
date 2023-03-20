package com.hescha.minijira.repository;

import com.hescha.minijira.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    List<Label> findByName(String name);

    List<Label> findByNameContains(String name);

    List<Label> findByDescription(String description);

    List<Label> findByDescriptionContains(String description);

    List<Label> findByIssuesContains(com.hescha.minijira.model.Issue issues);
}
