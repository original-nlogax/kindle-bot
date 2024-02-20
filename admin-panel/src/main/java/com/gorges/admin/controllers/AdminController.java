package com.gorges.admin.controllers;

import com.gorges.admin.models.entities.Admin;
import com.gorges.admin.models.entities.User;
import com.gorges.admin.services.AdminService;
import com.gorges.admin.services.UserService;
import com.gorges.admin.utils.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping
    public String showAllAdmins(Model model) {
        model.addAttribute("admins", adminService.findAll());
        return "main/admins/all";
    }


    @GetMapping("/add")
    public String showAddAdmin(Model model) {
        //model.addAttribute("roles", UserRole.values());
        return "main/admins/add";
    }

    @GetMapping("/edit/{admin}")
    public String showEditAdmin(@PathVariable Admin admin, Model model) {
        model.addAttribute("admin", admin);
        return "main/admins/edit";
    }


    @PostMapping("/create")
    public String createAdmin(@ModelAttribute("admin") Admin admin, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.mergeAttributes(ControllerUtils.findErrors(bindingResult));
            model.addAttribute("admin", admin);
            return "main/admins/add";
        }
        if (adminService.findByUsername(admin.getUsername()) != null) {
            model.addAttribute("usernameError", "Админ уже в списке");
            return "main/admins/add";
        }

        /*
        if (!admin.getUsername().startsWith("@")) {
            model.addAttribute("usernameError", "Никнейм должен начинаться с @");
            return "main/admins/add";
        }

        User user = userService.findByUsername(
            admin.getUsername().replace("@", ""));
        */
        User user = userService.findByUsername(admin.getUsername());

        if (user == null) {
                model.addAttribute("usernameError", "Пользователь не писал сообщения боту");
            return "main/admins/add";
        }

        admin.setChatId(user.getChatId());

        adminService.save(admin);
        return "redirect:/admins";
    }

    @PostMapping("/delete")
    public String deleteAdmin(@RequestParam Long chatId, Model model) {
        Admin admin = adminService.findByChatId(chatId);

        if (admin == null) {
            // todo handle
            System.out.println("admin deletion error: admin is null");
            return "redirect:/admins";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long deletorAdminChatId =
            ((com.gorges.admin.models.dto.User)(authentication.getPrincipal())).getId();

        if (deletorAdminChatId.equals(admin.getChatId())) {
            model.addAttribute("admin", admin);
            model.addAttribute("Error", "You cannot delete your account!");
            return "redirect:/admins";
        }

        adminService.deleteByChatId(chatId);
        return "redirect:/admins";
    }

}
