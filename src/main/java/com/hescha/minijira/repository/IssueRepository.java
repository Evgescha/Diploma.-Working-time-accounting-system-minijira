package com.hescha.minijira.repository;

import com.hescha.minijira.model.Column;
import com.hescha.minijira.model.Issue;
import com.hescha.minijira.model.IssueStatus;
import com.hescha.minijira.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByName(String name);

    List<Issue> findByNameContains(String name);

    List<Issue> findByDescription(String description);

    List<Issue> findByDescriptionContains(String description);

    Issue findByColumn(Column column);

    List<Issue> findByLabelsContains(com.hescha.minijira.model.Label labels);

    List<Issue> findByCommentsContains(com.hescha.minijira.model.Comment comments);

    Issue findByAssigned(User assigned);

    Issue findByCreated(User created);

    List<Issue> findByRelatedContains(com.hescha.minijira.model.Issue related);

    List<Issue> findByActivitiesContains(com.hescha.minijira.model.Activity activities);

    Issue findByTimeSpend(Integer timeSpend);

    Issue findByDateCreated(LocalDateTime dateCreated);

    Issue findByStatus(IssueStatus status);
}
