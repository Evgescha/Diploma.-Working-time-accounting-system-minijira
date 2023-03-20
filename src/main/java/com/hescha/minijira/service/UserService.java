package com.hescha.minijira.service;

import com.hescha.minijira.model.User;
import com.hescha.minijira.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class UserService extends CrudService<User> {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<User> findByFirstname(String firstname) {
        return repository.findByFirstname(firstname);
    }

    public List<User> findByFirstnameContains(String firstname) {
        return repository.findByFirstnameContains(firstname);
    }

    public List<User> findByLastname(String lastname) {
        return repository.findByLastname(lastname);
    }

    public List<User> findByLastnameContains(String lastname) {
        return repository.findByLastnameContains(lastname);
    }

    public List<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public List<User> findByUsernameContains(String username) {
        return repository.findByUsernameContains(username);
    }

    public List<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<User> findByEmailContains(String email) {
        return repository.findByEmailContains(email);
    }

    public List<User> findByPassword(String password) {
        return repository.findByPassword(password);
    }

    public List<User> findByPasswordContains(String password) {
        return repository.findByPasswordContains(password);
    }

    public List<User> findByImage(String image) {
        return repository.findByImage(image);
    }

    public List<User> findByImageContains(String image) {
        return repository.findByImageContains(image);
    }

    public List<User> findByOwnProjectsContains(com.hescha.minijira.model.Project ownProjects) {
        return repository.findByOwnProjectsContains(ownProjects);
    }

    public Set<User> findByContributeProjectsContains(com.hescha.minijira.model.Project contributeProjects) {
        return repository.findByContributeProjectsContains(contributeProjects);
    }

    public List<User> findByCommentsContains(com.hescha.minijira.model.Comment comments) {
        return repository.findByCommentsContains(comments);
    }

    public User findByDateCreated(LocalDateTime dateCreated) {
        return repository.findByDateCreated(dateCreated);
    }


    public User update(Long id, User entity) {
        User read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity User not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    private void updateFields(User entity, User read) {
        read.setFirstname(entity.getFirstname());
        read.setLastname(entity.getLastname());
        read.setUsername(entity.getUsername());
        read.setEmail(entity.getEmail());
        read.setPassword(entity.getPassword());
        read.setImage(entity.getImage());
        read.setOwnProjects(entity.getOwnProjects());
        read.setContributeProjects(entity.getContributeProjects());
        read.setComments(entity.getComments());
        read.setDateCreated(entity.getDateCreated());
    }
}
