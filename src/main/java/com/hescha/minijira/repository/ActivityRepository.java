package com.hescha.minijira.repository;

import com.hescha.minijira.model.Activity;
import com.hescha.minijira.model.ActivityType;
import com.hescha.minijira.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findByIssue(Issue issue);

    Activity findByType(ActivityType type);

    List<Activity> findByDescription(String description);

    List<Activity> findByDescriptionContains(String description);

    Activity findByDateCreated(LocalDateTime dateCreated);
}
