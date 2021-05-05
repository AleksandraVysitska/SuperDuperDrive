package com.udacity.jwdnd.course1.cloudstorage.controllers;


import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private List<File> files;
    private List<Note> notes;
    private  List<Credentials> credentials;
    private final UserService userService;
    private final FileService fileService;
    private final NotesService notesService;
    private final CredentialsService credentialsService;
    private final EncryptionService encryptionService;

    public HomeController(UserService userService, FileService fileService, NotesService notesService, CredentialsService credentialsService, EncryptionService encryptionService, EncryptionService encryptionService1) {
        this.userService = userService;
        this.fileService = fileService;
        this.notesService = notesService;
        this.credentialsService=credentialsService;
        this.encryptionService = encryptionService1;
    }

    @PostConstruct
    public void postConstruct(){
        files = new ArrayList<>();
        notes=new ArrayList<>();
        credentials=new ArrayList<>();
    }

    @GetMapping()
    public String homeView(Model model, Authentication authentication) throws Exception {
        int userId = userService.getUser(authentication.getName()).getUserId();
        files = fileService.getFilesForUser(userId);
        notes=notesService.getAllNotes(userId);
        credentials=credentialsService.getAllByUserId(Long.parseLong(userId+""));
        model.addAttribute("files", files);
        model.addAttribute("notes",notes);
        model.addAttribute("credentials",credentials);
        model.addAttribute("encryptionService",encryptionService);
        return "home";
    }


}
