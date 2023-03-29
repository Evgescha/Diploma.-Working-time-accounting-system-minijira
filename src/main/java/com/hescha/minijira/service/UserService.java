package com.hescha.minijira.service;

import com.hescha.minijira.model.User;
import com.hescha.minijira.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService extends CrudService<User> implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<User> filterUsers(String search) {
        return repository.findByUsernameContainsOrFirstnameContainsOrLastnameContains(search, search, search);
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

    public User findByUsername(String username) {
        log.info("findByUsername {}", username);
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
    }


    public boolean registerNew(User entity) {
        log.info("registerNew {}", entity);
        if (repository.findByUsername(entity.getUsername()) != null) {
            return false;
        }

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setDateCreated(LocalDateTime.now());
        try {
            create(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername {}", username);
        User user = repository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь " + username + " не был найден в базе");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), List.of());
    }
}
