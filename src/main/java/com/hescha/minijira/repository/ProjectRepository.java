package com.hescha.minijira.repository;

import com.hescha.minijira.model.Board;
import com.hescha.minijira.model.Project;
import com.hescha.minijira.model.ProjectStatusType;
import com.hescha.minijira.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByName(String name);

    List<Project> findByNameContains(String name);

    List<Project> findByDescription(String description);

    List<Project> findByDescriptionContains(String description);

    List<Project> findByImage(String image);

    List<Project> findByImageContains(String image);

    Project findByStatus(ProjectStatusType status);

    List<Project> findByMembersContains(com.hescha.minijira.model.User members);

    Project findByBoard(Board board);

    Project findByOwner(User owner);

    Project findByDateCreated(LocalDateTime dateCreated);
}
