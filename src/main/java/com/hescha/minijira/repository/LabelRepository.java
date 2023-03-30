package com.hescha.minijira.repository;

import com.hescha.minijira.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
}
