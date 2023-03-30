package com.hescha.minijira.repository;

import com.hescha.minijira.model.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {
}
