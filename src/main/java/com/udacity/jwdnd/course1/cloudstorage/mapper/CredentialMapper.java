package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM credentials")
    List<Credentials> findAll();

    @Select("SELECT * FROM credentials WHERE userid = #{userId}")
    List<Credentials> findByUserId(Long userId);

    @Select("SELECT * FROM credentials WHERE credentialid = #{id}")
    Credentials findById(Long id);

    @Insert("INSERT INTO credentials (url, username, key, password, userid) VALUES (#{credential.url}, #{credential.username}, #{credential.key}, #{credential.password}, #{userId})")
    Integer create(@Param("credential") Credentials credential, Long userId);

    @Update("UPDATE credentials SET url = #{credential.url}, username = #{credential.username}, key = #{credential.key}, password = #{credential.password} WHERE credentialid = #{credential.credentialId} AND userid = #{userId}")
    Integer update(@Param("credential") Credentials credential, Long userId);

    @Delete("DELETE FROM credentials WHERE credentialid = #{id} AND userid = #{userId}")
    Integer delete(Long id, Long userId);
}
