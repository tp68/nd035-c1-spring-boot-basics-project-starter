package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    // Dependency Injection
    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    /**
     * Retrieves all notes for a specific user.
     *
     * @param userId The ID of the currently logged-in user.
     * @return A list of Note objects.
     */
    public List<Note> getAllNotesForUser(Integer userId) {
        return noteMapper.getNotesForUser(userId);
    }

    /**
     * Saves or updates a note based on whether noteid is present.
     * Performs a basic validation check.
     *
     * @param note The Note object to save or update.
     * @return The number of affected rows (1 on success).
     */
    public int saveOrUpdateNote(Note note) {
        // Basic validation: Ensure essential fields are present
        if (note.getNotetitle() == null || note.getNotetitle().trim().isEmpty()) {
             // In a real application, you might throw an IllegalArgumentException
             return 0; 
        }

        // Check if this is an existing note (update) or a new one (insert)
        if (note.getNoteid() != null) {
            // Update existing note
            return noteMapper.updateNote(note);
        } else {
            // Insert new note
            return noteMapper.insertNote(note);
        }
    }

    /**
     * Deletes a note by its ID.
     *
     * @param noteId The ID of the note to delete.
     * @return The number of rows deleted.
     */
    public int deleteNote(Integer noteId) {
        return noteMapper.deleteNote(noteId);
    }

    /**
     * Retrieves a single note by its ID.
     */
    public Note getNote(Integer noteId) {
        return noteMapper.getNoteById(noteId);
    }
}
