package com.hescha.minijira.service;

import com.hescha.minijira.model.*;
import com.hescha.minijira.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService extends CrudService<Project> {

    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Project> findByNameContains(String name) {
        return repository.findByNameContains(name);
    }

    public Project update(Long id, Project entity) {
        Project read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Project not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    private void updateFields(Project entity, Project read) {
        read.setName(entity.getName());
        read.setDescription(entity.getDescription());
        read.setImage(entity.getImage());
        read.setStatus(entity.getStatus());
    }

    public List<UserStatistics> getUserStatistics(Integer projectId) {
        Project project = read(projectId);
        List<UserStatistics> userStatisticsList = new ArrayList<>();

        List<User> allUsers = new ArrayList<>(project.getMembers());
        allUsers.add(project.getOwner());

        for (User user : allUsers) {
            UserStatistics userStats = new UserStatistics();
            userStats.setUser(user);

            long assignedIssuesCount = project.getIssues().stream()
                    .filter(issue -> user.equals(issue.getAssigned()))
                    .count();
            userStats.setAssignedIssuesCount(assignedIssuesCount);

            long totalTimeSpent = project.getIssues().stream()
                    .filter(issue -> user.equals(issue.getAssigned()))
                    .mapToInt(Issue::getTimeSpend)
                    .sum();
            userStats.setTotalTimeSpent(totalTimeSpent);

            List<Issue> last5Issues = project.getIssues().stream()
                    .filter(issue -> user.equals(issue.getAssigned()))
                    .sorted(Comparator.comparing(Issue::getDateCreated).reversed())
                    .limit(5)
                    .collect(Collectors.toList());
            userStats.setLast5Issues(last5Issues);

            List<Activity> last10TimeActivities = project.getIssues().stream()
                    .flatMap(issue -> issue.getActivities().stream())
                    .filter(activity -> user.equals(activity.getOwner()))
                    .filter(activity -> activity.getType() == ActivityType.TIME_ADDED || activity.getType() == ActivityType.TIME_DELETED)
                    .sorted(Comparator.comparing(Activity::getDateCreated).reversed())
                    .limit(10)
                    .collect(Collectors.toList());
            userStats.setLast10TimeActivities(last10TimeActivities);

            userStatisticsList.add(userStats);
        }

        return userStatisticsList;
    }

    public Optional<Project> readOpt(long id) {
        return repository.findById(id);

    }
}
