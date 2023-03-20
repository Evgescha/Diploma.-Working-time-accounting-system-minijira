package com.hescha.minijira.repository;

import com.hescha.minijira.model.Comment;
import com.hescha.minijira.model.Issue;
import com.hescha.minijira.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByIssue(Issue issue);

    Comment findByOwner(User owner);

    List<Comment> findByText(String text);

    List<Comment> findByTextContains(String text);

    Comment findByDateCreated(LocalDateTime dateCreated);
}
