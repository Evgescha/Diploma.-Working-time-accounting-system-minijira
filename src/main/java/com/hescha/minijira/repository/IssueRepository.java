package com.hescha.minijira.repository;

import com.hescha.minijira.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
}
