package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface NoteMapper {

    /**
     * Retrieves all notes associated with a specific user ID.
     */
    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getNotesForUser(@Param("userId") Integer userId);

    /**
     * Retrieves a single note by its ID.
     */
    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note getNoteById(@Param("noteId") Integer noteId);

    /**
     * Inserts a new note into the database.
     * Uses @Options to return the auto-generated 'noteid'.
     */
    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) " +
            "VALUES (#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insertNote(Note note);

    /**
     * Updates an existing note based on its ID.
     */
    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid}")
    int updateNote(Note note);

    /**
     * Deletes a note by its ID.
     */
    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    int deleteNote(@Param("noteId") Integer noteId);
}
