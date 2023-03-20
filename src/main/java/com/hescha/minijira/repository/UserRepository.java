package com.hescha.minijira.repository;

import com.hescha.minijira.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByFirstname(String firstname);

    List<User> findByFirstnameContains(String firstname);

    List<User> findByLastname(String lastname);

    List<User> findByLastnameContains(String lastname);

    List<User> findByUsername(String username);

    List<User> findByUsernameContains(String username);

    List<User> findByEmail(String email);

    List<User> findByEmailContains(String email);

    List<User> findByPassword(String password);

    List<User> findByPasswordContains(String password);

    List<User> findByImage(String image);

    List<User> findByImageContains(String image);

    List<User> findByOwnProjectsContains(com.hescha.minijira.model.Project ownProjects);

    Set<User> findByContributeProjectsContains(com.hescha.minijira.model.Project contributeProjects);

    List<User> findByCommentsContains(com.hescha.minijira.model.Comment comments);

    User findByDateCreated(LocalDateTime dateCreated);
}
