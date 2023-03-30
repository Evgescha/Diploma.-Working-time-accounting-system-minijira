package com.hescha.minijira.service;

import com.hescha.minijira.model.Activity;
import com.hescha.minijira.model.Issue;
import com.hescha.minijira.model.User;
import com.hescha.minijira.model.Comment;
import com.hescha.minijira.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class CommentService extends CrudService<Comment> {

    private final CommentRepository repository;

    public CommentService(CommentRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Comment findByIssue(Issue issue) {
        return repository.findByIssue(issue);
    }

    public Comment findByOwner(User owner) {
        return repository.findByOwner(owner);
    }

    public List<Comment> findByText(String text) {
        return repository.findByText(text);
    }

    public List<Comment> findByTextContains(String text) {
        return repository.findByTextContains(text);
    }

    public Comment findByDateCreated(LocalDateTime dateCreated) {
        return repository.findByDateCreated(dateCreated);
    }


    public Comment update(Long id, Comment entity) {
        Comment read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Comment not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    public void deleteAll(Collection<Comment> list){
        repository.deleteAll(list);
    }

    private void updateFields(Comment entity, Comment read) {
        read.setIssue(entity.getIssue());
        read.setOwner(entity.getOwner());
        read.setText(entity.getText());
        read.setDateCreated(entity.getDateCreated());
    }
}
