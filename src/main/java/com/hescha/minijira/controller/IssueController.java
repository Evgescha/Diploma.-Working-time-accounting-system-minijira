package com.hescha.minijira.controller;

import com.hescha.minijira.model.Issue;
import com.hescha.minijira.model.Project;
import com.hescha.minijira.service.ColumnService;
import com.hescha.minijira.service.IssueService;
import com.hescha.minijira.service.ProjectService;
import com.hescha.minijira.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final SecurityService securityService;

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
        return THYMELEAF_TEMPLATE_ONE_ITEM_PAGE;
    }

    @PostMapping("/{id}/addTime")
    public String addTime(@PathVariable("id") Long id,
                          @RequestParam("timeAmount") Integer timeAmount) {
        Issue issue = issueService.read(id);
        Integer timeSpend = issue.getTimeSpend();
        timeSpend = timeSpend == null ? 0 : timeSpend;
        issue.setTimeSpend(timeSpend + timeAmount);
        service.update(issue);
        return REDIRECT_TO_ALL_ITEMS + "/get/" + issue.getId();
    }

    @PostMapping("/{id}/removeTime")
    public String removeTime(@PathVariable("id") Long id,
                             @RequestParam("timeAmount") Integer timeAmount) {
        Issue issue = issueService.read(id);
        Integer timeSpend = issue.getTimeSpend();
        timeSpend = timeSpend == null ? 0 : timeSpend;
        issue.setTimeSpend(timeSpend - timeAmount);
        service.update(issue);
        return REDIRECT_TO_ALL_ITEMS + "/get/" + issue.getId();
    }

    @GetMapping("/{id}")
    public String readProjectIssues(@PathVariable("id") Long id, Model model) {
        Project project = projectService.read(id);
        model.addAttribute("project", project);
        model.addAttribute("list", project.getIssues());
        return THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE;
    }

    @GetMapping(path = {"/{id}/edit", "/{id}/edit/{idLabel}"})
    public String editPage(Model model,
                           @PathVariable(name = "id") Long id,
                           @PathVariable(name = "idLabel", required = false) Long issueId) {
        Issue read;
        if (issueId == null) {
            read = new Issue();
        } else {
            read = service.read(issueId);
        }
        Project project = projectService.read(id);
        model.addAttribute("entity", read);
        model.addAttribute("labels", project.getLabels());
        model.addAttribute("project", project);
        return THYMELEAF_TEMPLATE_EDIT_PAGE;
    }

    @PostMapping
    public String save(@ModelAttribute Issue entity,
                       @RequestParam Integer projectId,
                       RedirectAttributes ra) {
        Project project = projectService.read(projectId);
        entity.setProject(project);
        if (entity.getId() == null) {
            try {
                entity.setTimeSpend(0);
                Issue createdEntity = service.create(entity);
                createdEntity.setCreated(securityService.getLoggedIn());
                project = projectService.read(projectId);
                project.getIssues().add(createdEntity);
                projectService.update(project);
                ra.addFlashAttribute(MESSAGE, "Creating is successful");
            } catch (Exception e) {
                ra.addFlashAttribute(MESSAGE, "Creating failed");
                e.printStackTrace();
            }
        } else {
            try {
                service.update(entity.getId(), entity);
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
            service.delete(id);
            projectService.delete(project.getId());
            ra.addFlashAttribute(MESSAGE, "Removing is successful");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute(MESSAGE, "Removing failed");
        }
        return REDIRECT_TO_ALL_ITEMS + "/" + projectId;
    }
}
