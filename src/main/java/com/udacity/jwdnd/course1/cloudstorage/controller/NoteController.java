package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/save")
    public String createNote(@ModelAttribute Note note, Model model, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);
        note.setUserId(user.getUserId());

        if (note.getNoteId() == null) {
            noteService.saveNote(note);
        } else {
            noteService.updateNote(note);
        }
        model.addAttribute("success", true);
        model.addAttribute("error", false);

        return "result";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, Authentication authentication, Model model) {
        User user = userService.getUserFromAuthentication(authentication);
        noteService.deleteNoteById(noteId, user.getUserId());

        model.addAttribute("success", true);
        model.addAttribute("error", false);


        return "result";
    }

}
