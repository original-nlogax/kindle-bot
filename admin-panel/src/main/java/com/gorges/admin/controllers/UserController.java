package com.gorges.admin.controllers;

import com.gorges.admin.models.entities.User;
import com.gorges.admin.services.UserService;
import com.gorges.admin.utils.ControllerUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "main/users/all";
    }

    @GetMapping("/edit/{user}")
    public String showEditUser(Model model, @PathVariable User user) {
        model.addAttribute("user", user);
        return "main/users/edit";
    }

    @PostMapping("/update")
    public String updateUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.mergeAttributes(ControllerUtils.findErrors(bindingResult));
            model.addAttribute("user", user);
            return "main/users/edit";
        }

        userService.update(user);
        return "redirect:/users/edit/" + user.getChatId();
    }

}
