package com.hescha.minijira.service;

import com.hescha.minijira.model.Column;
import com.hescha.minijira.repository.ColumnRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnService extends CrudService<Column> {

    private final ColumnRepository repository;

    public ColumnService(ColumnRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Column> findByName(String name) {
        return repository.findByName(name);
    }

    public List<Column> findByNameContains(String name) {
        return repository.findByNameContains(name);
    }

    public List<Column> findByIssuesContains(com.hescha.minijira.model.Issue issues) {
        return repository.findByIssuesContains(issues);
    }


    public Column update(Long id, Column entity) {
        Column read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Column not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    private void updateFields(Column entity, Column read) {
        read.setName(entity.getName());
        read.setIssues(entity.getIssues());
    }
}
