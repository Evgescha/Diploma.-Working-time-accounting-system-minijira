package com.hescha.minijira.service;

import com.hescha.minijira.model.Project;
import com.hescha.minijira.model.ProjectStatusType;
import com.hescha.minijira.model.User;
import com.hescha.minijira.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService extends CrudService<Project> {

    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Project> findByNameContains(String name) {
        return repository.findByNameContains(name);
    }

    public Project update(Long id, Project entity) {
        Project read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Project not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    private void updateFields(Project entity, Project read) {
        read.setName(entity.getName());
        read.setDescription(entity.getDescription());
        read.setImage(entity.getImage());
        read.setStatus(entity.getStatus());
    }
}
