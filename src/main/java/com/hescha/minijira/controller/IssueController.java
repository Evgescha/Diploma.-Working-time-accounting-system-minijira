package com.hescha.minijira.controller;

import com.hescha.minijira.model.Activity;
import com.hescha.minijira.model.ActivityType;
import com.hescha.minijira.model.Column;
import com.hescha.minijira.model.Comment;
import com.hescha.minijira.model.Issue;
import com.hescha.minijira.model.Project;
import com.hescha.minijira.model.User;
import com.hescha.minijira.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping(IssueController.CURRENT_ADDRESS)
public class IssueController {
    public static final String API_PATH = "issue";
    public static final String CURRENT_ADDRESS = "/" + API_PATH;
    public static final String MESSAGE = "message";
    public static final String THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE = API_PATH;
    public static final String THYMELEAF_TEMPLATE_ONE_ITEM_PAGE = THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE + "-one";
    public static final String THYMELEAF_TEMPLATE_EDIT_PAGE = THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE + "-edit";
    public static final String REDIRECT_TO_ALL_ITEMS = "redirect:" + CURRENT_ADDRESS;
    private final IssueService service;
    private final IssueService issueService;
    private final ProjectService projectService;
    private final ColumnService columnService;
    private final ActivityService activityService;
    private final SecurityService securityService;
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping
    public String readAllFromProject(Model model) {
        model.addAttribute("list", service.readAll());
        return THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE;
    }

    @GetMapping("/get/{id}")
    public String getIssueById(@PathVariable("id") Long id, Model model) {
        Issue issue = issueService.read(id);
        Project project = issue.getProject();
        model.addAttribute("project", project);
        model.addAttribute("entity", issue);

        Long loggedUserId = securityService.getLoggedIn().getId();
        model.addAttribute("isOwner", Objects.equals(loggedUserId, project.getOwner().getId()));
        User assigned = issue.getAssigned();
        boolean isApplicant = assigned != null && Objects.equals(assigned.getId(), loggedUserId);
        model.addAttribute("isApplicant", isApplicant);
        return THYMELEAF_TEMPLATE_ONE_ITEM_PAGE;
    }

    @PostMapping("/{id}/addTime")
    public String addTime(@PathVariable("id") Long id,
                          @RequestParam("timeAmount") Integer timeAmount) {
        Issue issue = issueService.read(id);
        Integer timeSpend = issue.getTimeSpend();
        timeSpend = timeSpend == null ? 0 : timeSpend;
        issue.setTimeSpend(timeSpend + timeAmount);
        Issue updatedIssue = service.update(issue);

        User loggedIn = securityService.getLoggedIn();

        Activity activity = new Activity();
        activity.setDescription(LocalDateTime.now() +": " + loggedIn.getUsername() + " added " + timeAmount +" minutes");
        activity.setIssue(issue);
        activity.setType(ActivityType.TIME_ADDED);
        activity.setOwner(loggedIn);

        Activity savedActivity = activityService.create(activity);
        updatedIssue.getActivities().add(savedActivity);
        issueService.update(updatedIssue);

        return REDIRECT_TO_ALL_ITEMS + "/get/" + issue.getId();
    }

    @PostMapping("/{id}/removeTime")
    public String removeTime(@PathVariable("id") Long id,
                             @RequestParam("timeAmount") Integer timeAmount) {
        Issue issue = issueService.read(id);
        Integer timeSpend = issue.getTimeSpend();
        timeSpend = timeSpend == null ? 0 : timeSpend;
        issue.setTimeSpend(timeSpend - timeAmount);
        Issue updatedIssue = service.update(issue);

        User loggedIn = securityService.getLoggedIn();

        Activity activity = new Activity();
        activity.setDescription(LocalDateTime.now() +": " + loggedIn.getUsername() + " removed " + timeAmount +" minutes");
        activity.setIssue(issue);
        activity.setType(ActivityType.TIME_DELETED);
        activity.setOwner(loggedIn);

        Activity savedActivity = activityService.create(activity);
        updatedIssue.getActivities().add(savedActivity);
        issueService.update(updatedIssue);
        return REDIRECT_TO_ALL_ITEMS + "/get/" + issue.getId();
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable("id") Long id,
                             @RequestParam("statusId") Integer statusId) {
        Issue updatedIssue = issueService.read(id);
        Column currentColumn = updatedIssue.getColumn();
        Column newColumn = columnService.read(statusId);
        updatedIssue.setColumn(newColumn);
        updatedIssue = service.update(updatedIssue);
        newColumn.getIssues().add(updatedIssue);
        columnService.update(newColumn);

        User loggedIn = securityService.getLoggedIn();

        Activity activity = new Activity();
        activity.setDescription(LocalDateTime.now() +": " + loggedIn.getUsername()
                + " change status from " + currentColumn.getName() +" to " + newColumn.getName());
        activity.setIssue(updatedIssue);
        activity.setType(ActivityType.STATUS_CHANGE);
        activity.setOwner(loggedIn);

        Activity savedActivity = activityService.create(activity);
        updatedIssue.getActivities().add(savedActivity);
        issueService.update(updatedIssue);
        return REDIRECT_TO_ALL_ITEMS + "/get/" + updatedIssue.getId();
    }

    @GetMapping("/{id}")
    public String readProjectIssues(@PathVariable("id") Long id, Model model) {
        Project project = projectService.read(id);
        model.addAttribute("project", project);
        model.addAttribute("list", project.getIssues());
        model.addAttribute("isOwner", Objects.equals(securityService.getLoggedIn().getId(), project.getOwner().getId()));
        return THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE;
    }

    @GetMapping(path = {"/{id}/edit", "/{id}/edit/{idLabel}"})
    public String editPage(Model model,
                           @PathVariable(name = "id") Long id,
                           @PathVariable(name = "idLabel", required = false) Long issueId) {
        Issue issue;
        if (issueId == null) {
            issue = new Issue();
        } else {
            issue = service.read(issueId);
        }
        Project project = projectService.read(id);
        model.addAttribute("entity", issue);
        model.addAttribute("labels", project.getLabels());
        model.addAttribute("project", project);
        return THYMELEAF_TEMPLATE_EDIT_PAGE;
    }

    @GetMapping(path = {"/{id}/assign/{userId}"})
    public String assignUser(Model model,
                           @PathVariable Long id,
                           @PathVariable Long userId) {
        Issue issue = service.read(id);
        User user = userService.read(userId);
        issue.setAssigned(user);
        service.update(issue);
        return REDIRECT_TO_ALL_ITEMS +"/get/" +id;
    }

    @PostMapping
    public String save(@ModelAttribute Issue entity,
                       @RequestParam Integer projectId,
                       RedirectAttributes ra) {
        Project project = projectService.read(projectId);
        entity.setProject(project);
        if (entity.getId() == null) {
            try {
                entity.setDateCreated(LocalDateTime.now());
                entity.setTimeSpend(0);
                entity.setCreated(securityService.getLoggedIn());
                Issue createdEntity = service.create(entity);
                project = projectService.read(projectId);
                project.getIssues().add(createdEntity);
                projectService.update(project);


                User loggedIn = securityService.getLoggedIn();

                Activity activity = new Activity();
                activity.setDescription(LocalDateTime.now() +": " + loggedIn.getUsername() + " created issue");
                activity.setIssue(createdEntity);
                activity.setType(ActivityType.COMMENT_ADD);
                activity.setOwner(loggedIn);

                Activity savedActivity = activityService.create(activity);
                createdEntity.getActivities().add(savedActivity);
                issueService.update(createdEntity);
                ra.addFlashAttribute(MESSAGE, "Creating is successful");
            } catch (Exception e) {
                ra.addFlashAttribute(MESSAGE, "Creating failed");
                e.printStackTrace();
            }
        } else {
            try {
                Issue updatedIssue = service.update(entity.getId(), entity);
                User loggedIn = securityService.getLoggedIn();

                Activity activity = new Activity();
                activity.setDescription(LocalDateTime.now() +": " + loggedIn.getUsername() + " updated issue");
                activity.setIssue(updatedIssue);
                activity.setType(ActivityType.COMMENT_ADD);
                activity.setOwner(loggedIn);

                Activity savedActivity = activityService.create(activity);
                updatedIssue.getActivities().add(savedActivity);
                issueService.update(updatedIssue);
                ra.addFlashAttribute(MESSAGE, "Editing is successful");
            } catch (Exception e) {
                e.printStackTrace();
                ra.addFlashAttribute(MESSAGE, "Editing failed");
            }
        }
        return REDIRECT_TO_ALL_ITEMS + "/" + projectId;
    }
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        Issue issue = issueService.read(id);
        Project project = issue.getProject();
        Long projectId = project.getId();
        try {
            project.getIssues().remove(issue);
            issue.setProject(null);
            List<Activity> activities = issue.getActivities();

            // Remove the relationship between Issue and Activity
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
            service.delete(id);
            ra.addFlashAttribute(MESSAGE, "Removing is successful");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute(MESSAGE, "Removing failed");
        }
        return REDIRECT_TO_ALL_ITEMS + "/" + projectId;
    }
}
