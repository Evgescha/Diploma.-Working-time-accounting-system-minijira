package com.hescha.minijira.controller;

import com.hescha.minijira.model.User;
import com.hescha.minijira.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class IndexController {
    private final UserService userService;

    @GetMapping
    public String indexPage() {
        return "redirect:/project";
    }

    @PostMapping
    public String indexPagePost() {
        return "redirect:/project";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationPost(User user, Model model) {
        log.info("Registration controller, new user: {}", user);
        boolean success = userService.registerNew(user);
        String response = success
                ? "Success"
                : "Registration failed. Try again later";
        model.addAttribute("success", response);
        return "registration";

    }
}
