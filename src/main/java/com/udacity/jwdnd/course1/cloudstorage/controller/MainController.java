package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final UserService userService;
    private final FileService fileService;
    private final NoteService noteService;

    public MainController(FileService fileService, UserService userService, NoteService noteService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping("/home")
    public String getHomePage(Authentication authentication, Model model) {
        User user = userService.getUserFromAuthentication(authentication);
        model.addAttribute("files", fileService.getFiles(user.getUserId()));
        model.addAttribute("notes", noteService.getNotes(user.getUserId()));
        return "home";
    }
}
