package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.service.UserService;

import javax.validation.Valid;

@Controller
public class UserPage extends Page {
    private final UserService userService;

    public UserPage(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = { "/user/{id}" })
    public String getUserPageById(@PathVariable @Valid  String id, Model model) {
        try {
            User user = userService.findById(Long.parseLong(id));
            if (user != null) {
                model.addAttribute("userInfo", user);
            }
            return "UserPage";
        } catch (NumberFormatException e) {
            return "IndexPage";
        }
    }
}
