package com.hescha.minijira.controller;

import com.hescha.minijira.model.Issue;
import com.hescha.minijira.model.Label;
import com.hescha.minijira.model.Project;
import com.hescha.minijira.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


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

    private final ColumnService columnService;
    private final LabelService labelService;
    private final CommentService commentService;
    private final UserService userService;
    private final IssueService issueService;
    private final ActivityService activityService;
    private final ProjectService projectService;

    @GetMapping
    public String readAllFromProject(Model model) {
        model.addAttribute("list", service.readAll());
        return THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE;
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
                Issue createdEntity = service.create(entity);
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
        try {
            service.delete(id);
            ra.addFlashAttribute(MESSAGE, "Removing is successful");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute(MESSAGE, "Removing failed");
        }
        return REDIRECT_TO_ALL_ITEMS;
    }
}
