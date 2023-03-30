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
