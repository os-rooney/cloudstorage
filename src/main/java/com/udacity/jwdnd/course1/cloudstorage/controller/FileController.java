package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileData, Authentication authentication, Model model) throws IOException {
        User user = userService.getUser(authentication.getName());
        Integer userId = user.getUserId();

        File file = fileService.setFileInformation(fileData, userId);

        if(fileService.fileExists(file.getFilename(), userId)) {
            model.addAttribute("success", false);
            model.addAttribute("error", true);
            return "result";
        }

        fileService.saveFile(file);
        model.addAttribute("success", true);

        return "result";
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") Integer fileId, Model model) {
        fileService.deleteFileById(fileId);
        model.addAttribute("success", true);
        model.addAttribute("error", false);
        return "result";
    }
}
