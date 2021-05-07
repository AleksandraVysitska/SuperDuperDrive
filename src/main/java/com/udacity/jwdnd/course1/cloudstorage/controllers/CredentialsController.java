package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialsController {
    private final CredentialsService credentialService;
    private final UserService userService;

    public CredentialsController(CredentialsService credentialsService, UserService userService) {
        this.credentialService = credentialsService;
        this.userService = userService;
    }
    @PostMapping(value = "/add-credential")
    public String addCredential(Authentication authentication,
                                RedirectAttributes redirectAttributes,
                                Credentials credential) {
        boolean result;
        try {
            if (credential.getCredentialId() == null) {
                result = credentialService.create(credential, getUserId(authentication));
            } else {
                result = credentialService.update(credential, getUserId(authentication));
            }

            if (!result) {
                redirectAttributes.addFlashAttribute("error", "Credential creation error");
                return "redirect:/result?error";
            }
            redirectAttributes.addFlashAttribute("success", "Credential was successfully created");
            return "redirect:/result?success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Insertion error: " + e.getMessage().toString());
            return "redirect:/result?error";
        }
    }

    @GetMapping(value = "/delete-credential/{id}")
    public String deleteCredential(Authentication authentication, @PathVariable("id") Long id) {
        boolean result = credentialService.delete(id, getUserId(authentication));

        if (!result) {
            return "redirect:/result?error";
        }

        return "redirect:/result?success";
    }

    private Long getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return Long.parseLong(user.getUserId().toString());
    }
}
