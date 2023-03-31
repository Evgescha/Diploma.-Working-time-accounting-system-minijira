package com.hescha.minijira.controller;

import com.hescha.minijira.model.*;
import com.hescha.minijira.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;


@Controller
@RequiredArgsConstructor
@RequestMapping(CommentController.CURRENT_ADDRESS)
public class CommentController {
    public static final String API_PATH = "comment";
    public static final String CURRENT_ADDRESS = "/" + API_PATH;
    public static final String MESSAGE = "message";
    public static final String THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE = API_PATH;
    public static final String THYMELEAF_TEMPLATE_ONE_ITEM_PAGE = THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE + "-one";
    public static final String THYMELEAF_TEMPLATE_EDIT_PAGE = THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE + "-edit";
    public static final String REDIRECT_TO_ALL_ITEMS = "redirect:" + CURRENT_ADDRESS;
    private final CommentService service;
    private final IssueService issueService;
    private final UserService userService;
    private final ActivityService activityService;
    private final SecurityService securityService;

    @GetMapping
    public String readAll(Model model) {
        model.addAttribute("list", service.readAll());
        return THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE;
    }

    @GetMapping("/{id}")
    public String read(@PathVariable("id") Long id, Model model) {
        model.addAttribute("entity", service.read(id));
        return THYMELEAF_TEMPLATE_ONE_ITEM_PAGE;
    }

    @GetMapping(path = {"/edit", "/edit/{id}"})
    public String editPage(Model model, @PathVariable(name = "id", required = false) Long id) {
        if (id == null) {
            model.addAttribute("entity", new Comment());
        } else {
            model.addAttribute("entity", service.read(id));
        }

        model.addAttribute("issue_list", issueService.readAll());
        model.addAttribute("user_list", userService.readAll());

        return THYMELEAF_TEMPLATE_EDIT_PAGE;
    }

    @PostMapping
    public String save(@ModelAttribute Comment entity,
                       @RequestParam("issueId") Long issueId,
                       RedirectAttributes ra) {
        Issue issue = issueService.read(issueId);
        if (entity.getId() == null) {
            try {
                User user = securityService.getLoggedIn();
                entity.setIssue(issue);
                entity.setOwner(user);
                entity.setDateCreated(LocalDateTime.now());

                Comment createdEntity = service.create(entity);
                issue.getComments().add(createdEntity);
                Issue updatedIssue = issueService.update(issue);

                Activity activity = new Activity();
                activity.setDescription(LocalDateTime.now() + ": " + user.getUsername() + " added a comment");
                activity.setIssue(issue);
                activity.setType(ActivityType.COMMENT_ADD);
                activity.setOwner(user);

                Activity savedActivity = activityService.create(activity);
                updatedIssue.getActivities().add(savedActivity);
                issueService.update(updatedIssue);
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
        return "redirect:/issue/get/" + issue.getId();
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
