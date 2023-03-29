package com.hescha.minijira.service;

import com.hescha.minijira.model.Column;
import com.hescha.minijira.model.Issue;
import com.hescha.minijira.model.IssueStatus;
import com.hescha.minijira.model.User;
import com.hescha.minijira.repository.IssueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IssueService extends CrudService<Issue> {

    private final IssueRepository repository;

    public IssueService(IssueRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Issue> findByName(String name) {
        return repository.findByName(name);
    }

    public List<Issue> findByNameContains(String name) {
        return repository.findByNameContains(name);
    }

    public List<Issue> findByDescription(String description) {
        return repository.findByDescription(description);
    }

    public List<Issue> findByDescriptionContains(String description) {
        return repository.findByDescriptionContains(description);
    }

    public Issue findByColumn(Column column) {
        return repository.findByColumn(column);
    }

    public List<Issue> findByLabelsContains(com.hescha.minijira.model.Label labels) {
        return repository.findByLabelsContains(labels);
    }

    public List<Issue> findByCommentsContains(com.hescha.minijira.model.Comment comments) {
        return repository.findByCommentsContains(comments);
    }

    public Issue findByAssigned(User assigned) {
        return repository.findByAssigned(assigned);
    }

    public Issue findByCreated(User created) {
        return repository.findByCreated(created);
    }

    public List<Issue> findByActivitiesContains(com.hescha.minijira.model.Activity activities) {
        return repository.findByActivitiesContains(activities);
    }

    public Issue findByTimeSpend(Integer timeSpend) {
        return repository.findByTimeSpend(timeSpend);
    }

    public Issue findByDateCreated(LocalDateTime dateCreated) {
        return repository.findByDateCreated(dateCreated);
    }

    public Issue findByStatus(IssueStatus status) {
        return repository.findByStatus(status);
    }


    public Issue update(Long id, Issue entity) {
        Issue read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Issue not found");
        }
        updateFields(entity, read);
        return update(read);
    }

    private void updateFields(Issue entity, Issue read) {
        read.setName(entity.getName());
        read.setDescription(entity.getDescription());
        read.setColumn(entity.getColumn());
        read.setLabels(entity.getLabels());
        read.setStatus(entity.getStatus());
    }
}
