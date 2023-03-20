package com.hescha.minijira.service;

import com.hescha.minijira.model.Label;
import com.hescha.minijira.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService extends CrudService<Label> {

    private final LabelRepository repository;

    public LabelService(LabelRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Label> findByName(String name) {
        return repository.findByName(name);
    }

    public List<Label> findByNameContains(String name) {
        return repository.findByNameContains(name);
    }

    public List<Label> findByDescription(String description) {
        return repository.findByDescription(description);
    }

    public List<Label> findByDescriptionContains(String description) {
        return repository.findByDescriptionContains(description);
    }

    public List<Label> findByIssuesContains(com.hescha.minijira.model.Issue issues) {
        return repository.findByIssuesContains(issues);
    }


    public Label update(Long id, Label entity) {
        Label read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Label not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    private void updateFields(Label entity, Label read) {
        read.setName(entity.getName());
        read.setDescription(entity.getDescription());
        read.setIssues(entity.getIssues());
    }
}
