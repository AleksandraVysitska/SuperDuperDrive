package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

    private UserService userService;
    private NotesService notesService;

    public NoteController(UserService userService, NotesService notesService) {
        this.userService = userService;
        this.notesService = notesService;
    }

    @PostMapping("/notes")
    public String createOrUpdateNote(Authentication authentication,
                                     @ModelAttribute Note note,
                                     RedirectAttributes redirectAttributes
                                     ) {
        boolean result;
        try {
            if (note.getNoteid() == 0) {
                if (notesService.checkNote(note)){
                    redirectAttributes.addFlashAttribute("error", "Note title or description already exists.");
                    return "redirect:/result?error";
                }
                result = notesService.addNote(note, getUserId(authentication));
            } else {
                if (notesService.checkNote(note)){
                    redirectAttributes.addFlashAttribute("error", "Note title or description already exists.");
                    return "redirect:/result?error";
                }
                result = notesService.updateNote(note);
            }

            if (!result) {
                redirectAttributes.addFlashAttribute("error", "Note creation error");
                return "redirect:/result?error";
            }
            redirectAttributes.addFlashAttribute("success", "Note was successfully created");


            return "redirect:/result?success";
        } catch(Exception e){
            redirectAttributes.addFlashAttribute("error", "Insertion error: " + e.getCause().toString());
            return "redirect:/result?error";
        }

    }




    @GetMapping("/notes/delete")
    public String deleteNote(@RequestParam("id") Integer noteid,  RedirectAttributes redirectAttributes) {
        if (noteid > 0) {
            notesService.deleteNote(noteid);
            redirectAttributes.addFlashAttribute("success", "Note was successfully deleted");

            return "redirect:/result?success";
        }
        redirectAttributes.addFlashAttribute("error", "Note deletion error");

        return "redirect:/result?error";
    }

    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }

}
