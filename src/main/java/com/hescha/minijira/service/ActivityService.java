package com.hescha.minijira.service;

import com.hescha.minijira.model.Activity;
import com.hescha.minijira.model.ActivityType;
import com.hescha.minijira.model.Issue;
import com.hescha.minijira.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityService extends CrudService<Activity> {

    private final ActivityRepository repository;

    public ActivityService(ActivityRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Activity findByIssue(Issue issue) {
        return repository.findByIssue(issue);
    }

    public Activity findByType(ActivityType type) {
        return repository.findByType(type);
    }

    public List<Activity> findByDescription(String description) {
        return repository.findByDescription(description);
    }

    public List<Activity> findByDescriptionContains(String description) {
        return repository.findByDescriptionContains(description);
    }

    public Activity findByDateCreated(LocalDateTime dateCreated) {
        return repository.findByDateCreated(dateCreated);
    }


    public Activity update(Long id, Activity entity) {
        Activity read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Activity not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    private void updateFields(Activity entity, Activity read) {
        read.setIssue(entity.getIssue());
        read.setType(entity.getType());
        read.setDescription(entity.getDescription());
        read.setDateCreated(entity.getDateCreated());
    }
}
