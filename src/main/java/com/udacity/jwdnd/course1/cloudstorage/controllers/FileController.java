package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping("/files")
public class FileController {

    private UserService userService;
    private FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }


    @PostMapping("/fileUpload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload,
                                   RedirectAttributes redirectAttributes,
                                   Authentication authentication) throws IOException, SQLException {
        try{
            if (fileUpload.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "File not selected to upload");
                return "redirect:/result?error";
            }
            Integer userId =  userService.getUser(authentication.getName()).getUserId();
            if (!fileService.checkFilename(fileUpload.getOriginalFilename(), userId)) {
                redirectAttributes.addFlashAttribute("error", "File already exists");
                return "redirect:/result?error";
            }
            fileService.loadFile(fileUpload,userId);
            redirectAttributes.addFlashAttribute("success", "File was successfully created");
            return "redirect:/result?success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Insertion error: " + e.getMessage().toString());
            return "redirect:/result?error";
        }
    }

    @GetMapping("/view/{fileid}")
    public ResponseEntity<ByteArrayResource> viewFile(@PathVariable("fileid") Integer fileid){
        File file=fileService.findFile(fileid);
        ByteArrayResource byteArrayResource=new ByteArrayResource(file.getFiledata());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getContenttype())) .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"") .body(new ByteArrayResource(file.getFiledata()));

    }

    @GetMapping("/delete/{fileid}")
    public String deleteFiles(@PathVariable("fileid")Integer fileid,Authentication authentication){
        fileService.deleteFile(fileid);
        return "redirect:/result?success";
    }


}
