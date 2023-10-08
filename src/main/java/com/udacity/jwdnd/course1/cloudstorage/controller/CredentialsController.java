package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/credentials")
public class CredentialsController {

    private final CredentialService credentialService;
    private final UserService userService;

    CredentialsController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/save")
    public String saveCredential(@ModelAttribute Credential credential, Model model, Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication);

        if (credentialService.credentialExists(credential.getUrl(), credential.getUsername())) {
            model.addAttribute("error", true);
            return "result";
        }

        Credential newCredential = credentialService.setCredentialInformation(credential);

        if (credential.getCredentialId() == null) {
            newCredential.setUserId(user.getUserId());
            credentialService.saveCredential(newCredential);
        } else {
            //credentialService.update(credential);
        }
        model.addAttribute("success", true);
        return "result";
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId, Authentication authentication, Model model) {
        User user = userService.getUserFromAuthentication(authentication);
        credentialService.deleteCredential(credentialId, user.getUserId());
        model.addAttribute("success", true);
        return "result";
    }
}
