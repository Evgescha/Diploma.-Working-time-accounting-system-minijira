package com.hescha.minijira.service;

import com.hescha.minijira.model.User;
import com.hescha.minijira.repository.UserRepository;
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

    public User findByUsername(String username) {
        log.info("findByUsername {}", username);
        return repository.findByUsername(username);
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
