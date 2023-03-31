package com.hescha.minijira.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hescha.minijira.model.Project;
import com.hescha.minijira.model.User;
import com.hescha.minijira.model.UserStatistics;
import com.hescha.minijira.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


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
    private final IssueService issueService;
    private final ColumnService columnService;
    private final LabelService labelService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

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
    public String read(@PathVariable("id") Long id, Model model) throws Exception {
        Optional<Project> projectOpt = service.readOpt(id);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            model.addAttribute("project", project);
            model.addAttribute("users", List.of());
            String membersJson = objectMapper.writeValueAsString(project.getMembers());
            model.addAttribute("membersJson", membersJson);
            Long loggedUserId = securityService.getLoggedIn().getId();
            model.addAttribute("isOwner", Objects.equals(loggedUserId, project.getOwner().getId()));
        } else {
            model.addAttribute("project", new Project());
        }
        return THYMELEAF_TEMPLATE_ONE_ITEM_PAGE;
    }

    @GetMapping("/{id}/user-statistics")
    public String showUserStatistics(@PathVariable Integer id, Model model) {
        Project project = service.read(id);
        model.addAttribute("project", project);
        List<UserStatistics> userStatistics = service.getUserStatistics(id);
        model.addAttribute("userStatisticsList", userStatistics);
        return "user-statistics";
    }

    @GetMapping("/{id}/addmember/{userId}")
    public String addMember(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        User user = userService.read(userId);
        Project project = service.read(id);
        project.getMembers().add(user);

        service.update(project);
        user.getContributeProjects().add(project);
        userService.update(user);

        return "redirect:" + CURRENT_ADDRESS + "/" + id;
    }

    @GetMapping("/{id}/removemember/{userId}")
    public String removeMember(@PathVariable("id") Long id, @PathVariable("userId") Long userId, Model model) {
        Project project = service.read(id);

        User userFromMembers = project.getMembers().stream()
                .filter(user1 -> Objects.equals(user1.getId(), userId))
                .findFirst()
                .orElse(null);
        if (userFromMembers != null) {
            project.getMembers().remove(userFromMembers);
            service.update(project);
        }
        User user = userService.read(userId);
        Project projectFromContribution = user.getContributeProjects().stream()
                .filter(project1 -> Objects.equals(project1.getId(), id))
                .findFirst()
                .orElse(null);
        if (projectFromContribution != null) {
            user.getContributeProjects().remove(projectFromContribution);
            userService.update(user);
        }
        return "redirect:" + CURRENT_ADDRESS + "/" + id;
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
            tryDelete(id);
            delete(id);
            ra.addFlashAttribute(MESSAGE, "Removing is successful");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute(MESSAGE, "Removing failed");
        }
        return REDIRECT_TO_ALL_ITEMS;
    }

    private void tryDelete(Long id) {
        for (int i = 0; i < 5; i++) {
            try {
                delete(id);
            } catch (Exception e) {
            }
        }
    }

    private void delete(Long id) {
        Optional<Project> opt = service.readOpt(id);
        if (opt.isEmpty()) return;
        Project project = opt.get();
        var modelMap = new RedirectAttributesModelMap();
        project.getLabels().forEach(entity -> labelService.delete(entity.getId(), modelMap));
        project.getIssues().forEach(entity -> issueService.delete(entity.getId(), modelMap));
        project.getColumns().forEach(entity -> columnService.delete(entity.getId(), modelMap));

        project = service.read(id);
        for (User member : project.getMembers()) {
            member.getContributeProjects().remove(project);
            userService.update(member);
        }

        project = service.read(id);
        project.setMembers(Collections.emptyList());
        User owner = project.getOwner();
        if (owner != null) {
            owner.getOwnProjects().remove(project);
            userService.update(owner);
            project = service.read(id);
        }
        project.setOwner(null);
        service.update(project);

        service.delete(id);
    }

}
