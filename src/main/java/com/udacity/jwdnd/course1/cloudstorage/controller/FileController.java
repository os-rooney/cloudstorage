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

import javax.servlet.http.HttpServletResponse;
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
        User user = userService.getUserFromAuthentication(authentication);
        Integer userId = user.getUserId();

        if (fileService.isFileEmpty(fileData, model)) {
            return "result";
        }

        File file = fileService.setFileInformation(fileData, userId);

        if (fileService.isFileAlreadyUploaded(file, model)) {
            return "result";
        }

        fileService.saveFile(file, model);

        return "result";
    }

    @GetMapping("/view/{filename}")
    public void viewFile(@PathVariable("filename") String filename, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = userService.getUserFromAuthentication(authentication);
        File file = fileService.getFileByName(filename, user.getUserId());

        if (file != null) {
            fileService.setResponseHeaders(response, file);
            fileService.writeToFileOutputStream(response, file);
        } else {
            // Handle the case when the file is not found
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") Integer fileId, Model model) {
        fileService.deleteFileById(fileId);
        model.addAttribute("success", true);
        model.addAttribute("error", false);
        return "result";
    }
}
