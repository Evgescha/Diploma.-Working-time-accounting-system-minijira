package com.hescha.minijira.service;

import com.hescha.minijira.model.*;
import com.hescha.minijira.repository.IssueRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.hescha.minijira.controller.IssueController.MESSAGE;

@Service
public class IssueService extends CrudService<Issue> {

    private final IssueRepository repository;
    private final ProjectService projectService;
    private final ActivityService activityService;

    public IssueService(IssueRepository repository,
                        ProjectService projectService,
                        ActivityService activityService) {
        super(repository);
        this.repository = repository;
        this.projectService = projectService;
        this.activityService = activityService;
    }

    public Issue update(Long id, Issue entity) {
        Issue read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Issue not found");
        }
        updateFields(entity, read);
        return update(read);
    }

    private void updateFields(Issue entity, Issue read) {
        read.setName(entity.getName());
        read.setDescription(entity.getDescription());
        read.setColumn(entity.getColumn());
        read.setLabels(entity.getLabels());
        read.setStatus(entity.getStatus());
    }

    public Long delete(Long id, RedirectAttributes ra) {
        Issue issue = read(id);
        Project project = issue.getProject();
        Long projectId = project.getId();
        try {
            project.getIssues().remove(issue);
            issue.setProject(null);
            issue.setColumn(null);
            List<Activity> activities = issue.getActivities();

            for (Activity activity : activities) {
                activity.setIssue(null);
            }
            activityService.deleteAll(activities);
            List<Comment> comments = issue.getComments();
            for (Comment comment:comments){
                comment.setOwner(null);
                comment.setIssue(null);
            }
            comments.removeAll(comments);

            projectService.update(project);
            delete(id);
            ra.addFlashAttribute(MESSAGE, "Removing is successful");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute(MESSAGE, "Removing failed");
        }
        return projectId;
    }
}
