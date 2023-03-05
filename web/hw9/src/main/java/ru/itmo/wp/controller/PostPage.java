package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.PostService;
import ru.itmo.wp.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@Controller
public class PostPage extends Page {
    private final PostService postService;
    private final UserService userService;

    public PostPage(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @Guest
    @GetMapping("/post/{id}")
    public String postGet(@PathVariable("id") String id, Model model) {
        try {
            long longId = Long.parseLong(id);
            Post post;
            post = postService.findById(longId);
            if (post == null) {
                throw new NullPointerException();
            }
            model.addAttribute("post", post);
            model.addAttribute("comment", new Comment());
            return "PostPage";
        } catch (NumberFormatException | NullPointerException exception) {
            return "redirect:/";
        }
    }

    @PostMapping("/post/{id}")
    public String postPost(@PathVariable("id") String id, @Valid @ModelAttribute("comment") Comment comment,
                           BindingResult bindingResult, Model model,
                           HttpSession httpSession) {
        try {
            long longId = Long.parseLong(id);
            Post post;
            post = postService.findById(longId);
            if (post == null) {
                throw new NullPointerException();
            }
            if (bindingResult.hasErrors()) {
                model.addAttribute("post", post);
                return "PostPage";
            }
            userService.writeComment(getUser(httpSession), post, comment);
            putMessage(httpSession, "You published new Comment");
            return "redirect:/post/" + id;
        } catch (NumberFormatException | NullPointerException e) {
            return "redirect:/";
        }
    }

}