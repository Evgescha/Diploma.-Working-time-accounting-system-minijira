package com.hescha.minijira.repository;

import com.hescha.minijira.model.Board;
import com.hescha.minijira.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByProject(Project project);

    List<Board> findByColumnsContains(com.hescha.minijira.model.Column columns);

    List<Board> findByIssuesContains(com.hescha.minijira.model.Issue issues);

    List<Board> findByLabelsContains(com.hescha.minijira.model.Label labels);
}
