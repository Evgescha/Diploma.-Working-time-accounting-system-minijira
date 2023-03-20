package com.hescha.minijira.repository;

import com.hescha.minijira.model.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {
    List<Column> findByName(String name);

    List<Column> findByNameContains(String name);

    List<Column> findByIssuesContains(com.hescha.minijira.model.Issue issues);
}
