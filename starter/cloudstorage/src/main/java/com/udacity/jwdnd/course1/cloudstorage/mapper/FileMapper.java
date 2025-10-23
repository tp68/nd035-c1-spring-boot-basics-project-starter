package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;
import java.util.List;

@Mapper
public interface FileMapper {

    /**
     * Inserts a new file record into the database.
     * Uses @Options to retrieve the auto-generated 'fileId' and set it back on the File object.
     */
    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    /**
     * Retrieves all file metadata (excluding the large 'filedata' byte array) for a specific user.
     */
    @Select("SELECT fileId, filename, contenttype, filesize, userid FROM FILES WHERE userid = #{userId}")
    List<File> getFilesByUser(@Param("userId") Integer userId);

    /**
     * Retrieves the complete file record (including binary 'filedata') by its ID.
     */
    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFileById(@Param("fileId") Integer fileId);

    /**
     * Retrieves the complete file record by file name and user ID (useful for validation).
     */
    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    File getFileByNameAndUser(@Param("fileName") String fileName, @Param("userId") Integer userId);
    
    /**
     * Deletes a file record by its ID.
     */
    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFileById(@Param("fileId") Integer fileId);
}
