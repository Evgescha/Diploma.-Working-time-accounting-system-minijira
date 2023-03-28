package com.hescha.minijira.controller;

import com.hescha.minijira.model.Project;
import com.hescha.minijira.model.User;
import com.hescha.minijira.service.ProjectService;
import com.hescha.minijira.service.SecurityService;
import com.hescha.minijira.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping(ProjectController.CURRENT_ADDRESS)
public class ProjectController {
    public static final String API_PATH = "project";
    public static final String CURRENT_ADDRESS = "/" + API_PATH;
    public static final String MESSAGE = "message";
    public static final String THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE = API_PATH;
    public static final String THYMELEAF_TEMPLATE_ONE_ITEM_PAGE = THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE + "-one";
    public static final String THYMELEAF_TEMPLATE_EDIT_PAGE = THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE + "-edit";
    public static final String REDIRECT_TO_ALL_ITEMS = "redirect:" + CURRENT_ADDRESS;

    private final ProjectService service;
    private final UserService userService;
    private final SecurityService securityService;

    @GetMapping
    public String readAll(Model model, @RequestParam(value = "searchPhrase", required = false) String searchPhrase) {
        if (searchPhrase == null) {
            model.addAttribute("list", service.readAll());
        } else {
            model.addAttribute("list", service.findByNameContains(searchPhrase));
        }

        return THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE;
    }

    @GetMapping("/{id}")
    public String read(@PathVariable("id") Long id, Model model) {
        model.addAttribute("project", service.read(id));
        return THYMELEAF_TEMPLATE_ONE_ITEM_PAGE;
    }

    @GetMapping(path = {"/edit", "/edit/{id}"})
    public String editPage(Model model, @PathVariable(name = "id", required = false) Long id) {
        if (id == null) {
            model.addAttribute("project", new Project());
        } else {
            model.addAttribute("project", service.read(id));
        }

        model.addAttribute("projectStatusType_list", List.of());
        return THYMELEAF_TEMPLATE_EDIT_PAGE;
    }

    @PostMapping
    public String save(@ModelAttribute Project entity, RedirectAttributes ra) {
        if (entity.getId() == null) {
            return createEntity(entity, ra);
        } else {
            return updateEntity(entity, ra);
        }
    }

    private String updateEntity(Project entity, RedirectAttributes ra) {
        try {
            service.update(entity.getId(), entity);
            ra.addFlashAttribute(MESSAGE, "Editing is successful");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute(MESSAGE, "Editing failed");
        }
        return REDIRECT_TO_ALL_ITEMS + "/" + entity.getId();
    }

    private String createEntity(Project entity, RedirectAttributes ra) {
        try {
            User owner = securityService.getLoggedIn();
            entity.setOwner(owner);
            entity.setDateCreated(LocalDateTime.now());
            Project createdEntity = service.create(entity);
            owner.getOwnProjects().add(createdEntity);
            userService.update(owner);
            ra.addFlashAttribute(MESSAGE, "Creating is successful");
            return REDIRECT_TO_ALL_ITEMS + "/" + createdEntity.getId();
        } catch (Exception e) {
            ra.addFlashAttribute(MESSAGE, "Creating failed");
            e.printStackTrace();
        }
        return REDIRECT_TO_ALL_ITEMS;
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
