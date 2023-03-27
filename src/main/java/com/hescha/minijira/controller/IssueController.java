package com.hescha.minijira.controller;

import com.hescha.minijira.model.Issue;
import com.hescha.minijira.service.ActivityService;
import com.hescha.minijira.service.ColumnService;
import com.hescha.minijira.service.CommentService;
import com.hescha.minijira.service.IssueService;
import com.hescha.minijira.service.LabelService;
import com.hescha.minijira.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
            model.addAttribute("entity", new Issue());
        } else {
            model.addAttribute("entity", service.read(id));
        }

        model.addAttribute("column_list", columnService.readAll());
        model.addAttribute("label", labelService.readAll());
        model.addAttribute("comment", commentService.readAll());
        model.addAttribute("user_list", userService.readAll());
        model.addAttribute("issue", issueService.readAll());
        model.addAttribute("activity", activityService.readAll());
        model.addAttribute("issueStatus_list", List.of());

        return THYMELEAF_TEMPLATE_EDIT_PAGE;
    }

    @PostMapping
    public String save(@ModelAttribute Issue entity, RedirectAttributes ra) {
        if (entity.getId() == null) {
            try {
                Issue createdEntity = service.create(entity);
                ra.addFlashAttribute(MESSAGE, "Creating is successful");
                return REDIRECT_TO_ALL_ITEMS + "/" + createdEntity.getId();
            } catch (Exception e) {
                ra.addFlashAttribute(MESSAGE, "Creating failed");
                e.printStackTrace();
            }
            return REDIRECT_TO_ALL_ITEMS;
        } else {
            try {
                service.update(entity.getId(), entity);
                ra.addFlashAttribute(MESSAGE, "Editing is successful");
            } catch (Exception e) {
                e.printStackTrace();
                ra.addFlashAttribute(MESSAGE, "Editing failed");
            }
            return REDIRECT_TO_ALL_ITEMS + "/" + entity.getId();
        }
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
