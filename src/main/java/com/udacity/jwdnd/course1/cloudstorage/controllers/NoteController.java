package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {

    private UserService userService;
    private NotesService notesService;

    public NoteController(UserService userService, NotesService notesService) {
        this.userService = userService;
        this.notesService = notesService;
    }

    @PostMapping("/notes")
    public String createOrUpdateNote(Authentication authentication, Note note) {
        boolean result;

        if (note.getNoteid()==0) {
            result = notesService.addNote(note, getUserId(authentication));
        } else {
            result = notesService.updateNote(note);
        }
        if (!result) {
            return "redirect:/result?error";
        }

        return "redirect:/result?success";
    }



    @GetMapping("/notes/delete")
    public String deleteNote(@RequestParam("id") int noteid) {
        if (noteid > 0) {
            notesService.deleteNote(noteid);
            return "redirect:/result?success";
        }
        return "redirect:/result?error";
    }

    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }

    private Note convertNote(NoteForm note, Authentication authentication) {
        Note n = new Note();
        if (note.getNoteid()!=null && !note.getNoteid().isBlank()){
            n.setNoteid(Integer.parseInt(note.getNoteid().toString()));
            n.setUserid(note.getUserid());
        } else {
            n.setUserid(getUserId(authentication));
        }
        n.setNotetitle(note.getNotetitle());
        n.setNotedescription(note.getNotedescription());

        return n;
    }
}
