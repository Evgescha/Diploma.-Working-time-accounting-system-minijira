package com.hescha.minijira.service;

import com.hescha.minijira.model.Board;
import com.hescha.minijira.model.Project;
import com.hescha.minijira.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService extends CrudService<Board> {

    private final BoardRepository repository;

    public BoardService(BoardRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Board findByProject(Project project) {
        return repository.findByProject(project);
    }

    public List<Board> findByColumnsContains(com.hescha.minijira.model.Column columns) {
        return repository.findByColumnsContains(columns);
    }

    public List<Board> findByIssuesContains(com.hescha.minijira.model.Issue issues) {
        return repository.findByIssuesContains(issues);
    }

    public List<Board> findByLabelsContains(com.hescha.minijira.model.Label labels) {
        return repository.findByLabelsContains(labels);
    }


    public Board update(Long id, Board entity) {
        Board read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Board not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    private void updateFields(Board entity, Board read) {
        read.setProject(entity.getProject());
        read.setColumns(entity.getColumns());
        read.setIssues(entity.getIssues());
        read.setLabels(entity.getLabels());
    }
}
