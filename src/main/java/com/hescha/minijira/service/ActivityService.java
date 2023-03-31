package com.hescha.minijira.service;

import com.hescha.minijira.model.Activity;
import com.hescha.minijira.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ActivityService extends CrudService<Activity> {

    private final ActivityRepository repository;

    public ActivityService(ActivityRepository repository) {
        super(repository);
        this.repository = repository;
    }


    public Activity update(Long id, Activity entity) {
        Activity read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Activity not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    public void deleteAll(Collection<Activity> activities) {
        repository.deleteAll(activities);
    }

    private void updateFields(Activity entity, Activity read) {
        read.setIssue(entity.getIssue());
        read.setType(entity.getType());
        read.setDescription(entity.getDescription());
        read.setDateCreated(entity.getDateCreated());
    }
}
