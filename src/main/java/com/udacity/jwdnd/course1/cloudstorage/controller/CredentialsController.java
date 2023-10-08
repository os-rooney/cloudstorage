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

        String encodeKey = credentialService.createEncryptedKey();
        String encryptedPassword = credentialService.createEncryptedPassword(credential.getPassword(), encodeKey);
        credential.setKey(encodeKey);
        credential.setPassword(encryptedPassword);

        if (credential.getCredentialId() == null) {
            credential.setUserId(user.getUserId());
            credentialService.saveCredential(credential);
        } else {
            credentialService.updateCredential(credential);
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
