package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CredentialMapper {

    /**
     * Retrieves all credentials for a specific user ID.
     */
    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getCredentialsForUser(@Param("userId") Integer userId);

    /**
     * Retrieves a single credential by its ID.
     */
    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    Credential getCredentialById(@Param("credentialId") Integer credentialId);

    /**
     * Inserts a new credential into the database.
     * Uses @Options to return the auto-generated 'credentialid'.
     */
    @Insert("INSERT INTO CREDENTIALS (url, username, salt, password, userid) " +
            "VALUES (#{url}, #{username}, #{salt}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insertCredential(Credential credential);

    /**
     * Updates an existing credential based on its ID.
     */
    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, " +
            "salt = #{salt}, password = #{password} " + 
            "WHERE credentialid = #{credentialid}")
    int updateCredential(Credential credential);

    /**
     * Deletes a credential by its ID.
     */
    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    int deleteCredential(@Param("credentialId") Integer credentialId);
}
