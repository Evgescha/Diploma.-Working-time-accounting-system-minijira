package com.hescha.minijira.repository;

import com.hescha.minijira.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsernameContainsOrFirstnameContainsOrLastnameContains(String username, String firstname, String lastname);

    User findByUsername(String username);

}
