package com.hescha.minijira.controller;

import com.hescha.minijira.model.Label;
import com.hescha.minijira.model.Project;
import com.hescha.minijira.service.LabelService;
import com.hescha.minijira.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@RequestMapping(LabelController.CURRENT_ADDRESS)
public class LabelController {
    public static final String API_PATH = "label";
    public static final String CURRENT_ADDRESS = "/" + API_PATH;
    public static final String MESSAGE = "message";
    public static final String THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE = API_PATH;
    public static final String THYMELEAF_TEMPLATE_EDIT_PAGE = THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE + "-edit";
    public static final String REDIRECT_TO_ALL_ITEMS = "redirect:" + CURRENT_ADDRESS;

    private final LabelService labelService;

    private final ProjectService projectService;


    @GetMapping
    public String readAll(Model model) {
        model.addAttribute("list", labelService.readAll());
        return THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE;
    }

    @GetMapping("/{id}")
    public String readProjectLabels(@PathVariable("id") Long id, Model model) {
        Project project = projectService.read(id);
        model.addAttribute("project", project);
        model.addAttribute("list", project.getLabels());
        return THYMELEAF_TEMPLATE_ALL_ITEMS_PAGE;
    }

    @GetMapping(path = {"/{id}/edit", "/{id}/edit/{idLabel}"})
    public String editPage(Model model,
                           @PathVariable(name = "id") Long id,
                           @PathVariable(name = "idLabel", required = false) Long idLabel) {
        if (idLabel == null) {
            model.addAttribute("entity", new Label());
        } else {
            model.addAttribute("entity", labelService.read(idLabel));
        }
        model.addAttribute("project", projectService.read(id));
        return THYMELEAF_TEMPLATE_EDIT_PAGE;
    }

    @PostMapping
    public String save(@ModelAttribute Label entity,
                       @RequestParam Integer projectId,
                       RedirectAttributes ra) {
        Project project = projectService.read(projectId);
        entity.setProject(project);
        if (entity.getId() == null) {
            try {
                Label createdEntity = labelService.create(entity);
                project.getLabels().add(createdEntity);
                projectService.update(project);
                ra.addFlashAttribute(MESSAGE, "Creating is successful");
                return REDIRECT_TO_ALL_ITEMS + "/" + createdEntity.getId();
            } catch (Exception e) {
                ra.addFlashAttribute(MESSAGE, "Creating failed");
                e.printStackTrace();
            }
            return REDIRECT_TO_ALL_ITEMS;
        } else {
            try {
                labelService.update(entity.getId(), entity);
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
        Label label = labelService.read(id);
        Project project = label.getProject();
        try {
            project.getLabels().remove(label);
            projectService.update(project);
            label.setProject(null);
            labelService.delete(label.getId());
            ra.addFlashAttribute(MESSAGE, "Removing is successful");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute(MESSAGE, "Removing failed");
        }
        return REDIRECT_TO_ALL_ITEMS + "/" + project.getId();
    }
}
