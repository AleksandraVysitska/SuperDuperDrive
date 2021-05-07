package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("Select * from Files where userid=#{userid} ")
    List<File> getAllFiles(int userid);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileid")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileid}")
    void delete(Integer fileid);

    @Delete("Delete from files where userid=#{userid}")
    void deleteAllFiles(int userid);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileid}")
    File getFile(Integer fileid);

    @Select("SELECT * FROM files WHERE filename = #{fileName} and userid = #{userid}")
    File getFileByName(String fileName, Integer userid);
}
