package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.JwtUser;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;
    private UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public void loadFile(MultipartFile file, Integer userId) throws IOException, SQLException {
        InputStream is = file.getInputStream();
        File f = new File();
        try {

            f.setFilename(file.getOriginalFilename());
            f.setContenttype(file.getContentType());
            f.setFilesize(file.getSize() + "");
            f.setUserid(userId);
            f.setFiledata(file.getBytes());

            fileMapper.insert(f);
        } finally {
            is.close();
        }
    }

    public void deleteFile(Integer fileid){
        fileMapper.delete(fileid);
        System.out.println(fileid);
    }

    public File findFile(Integer fileid) {
        return fileMapper.getFile(fileid);
    }

    public List<File> getFilesForUser(Integer userId) {
        return fileMapper.getAllFiles(userId);
    }

    public boolean checkFilename(String fileName, Integer userId) {

        return ((fileMapper.getFileByName(fileName, userId) == null) ? true : false);

    }
}
