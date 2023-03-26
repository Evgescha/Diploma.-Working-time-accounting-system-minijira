package com.hescha.minijira.service;

import com.hescha.minijira.model.Board;
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

    public List<Project> findByName(String name) {
        return repository.findByName(name);
    }

    public List<Project> findByNameContains(String name) {
        return repository.findByNameContains(name);
    }

    public List<Project> findByDescription(String description) {
        return repository.findByDescription(description);
    }

    public List<Project> findByDescriptionContains(String description) {
        return repository.findByDescriptionContains(description);
    }

    public List<Project> findByImage(String image) {
        return repository.findByImage(image);
    }

    public List<Project> findByImageContains(String image) {
        return repository.findByImageContains(image);
    }

    public Project findByStatus(ProjectStatusType status) {
        return repository.findByStatus(status);
    }

    public List<Project> findByMembersContains(com.hescha.minijira.model.User members) {
        return repository.findByMembersContains(members);
    }

    public Project findByBoard(Board board) {
        return repository.findByBoard(board);
    }

    public Project findByOwner(User owner) {
        return repository.findByOwner(owner);
    }

    public Project findByDateCreated(LocalDateTime dateCreated) {
        return repository.findByDateCreated(dateCreated);
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
