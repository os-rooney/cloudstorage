package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.SignupDTO;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignupPage(Model model) {
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setSignupSucess(false);
        signupDTO.setUsernameExistsAlready(false);
        model.addAttribute("signupDTO", signupDTO);
        return "signup";
    }

    @PostMapping
    public String handleSubmit(@ModelAttribute("signupDTO") SignupDTO signupDTO, Model model) {
        if (userService.usernameIsAvailable(signupDTO.getUsername())) {
            User user = new User(signupDTO.getFirstname(), signupDTO.getLastname(), signupDTO.getUsername(), signupDTO.getPassword());
            int rowAdded = userService.createUser(user);
            if(rowAdded > 0) {
                signupDTO.setSignupSucess(true);
            }
            signupDTO.setUsernameExistsAlready(false);
        } else {
            signupDTO.setSignupSucess(false);
            signupDTO.setUsernameExistsAlready(true);
        }

        model.addAttribute("signupDTO", signupDTO);
        return "signup";
    }
}
