package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication) throws IOException, SQLException {
        fileService.loadFile(fileUpload,userService.getUser(authentication.getName()).getUserId());
        return "redirect:/result?success";
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
