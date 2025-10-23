package com.udacity.jwdnd.course1.cloudstorage.model;

//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;

public class NoteForm {

    // Muss Integer sein, um 'null' zu akzeptieren, wenn es sich um eine NEUE Notiz handelt.
    private Integer noteId; 

    //@NotBlank(message = "Title cannot be blank")
    //@Size(max = 20, message = "Title must be 20 characters or less")
    private String noteTitle;

    //@NotBlank(message = "Description cannot be blank")
    //@Size(max = 1000, message = "Description must be 1000 characters or less")
    private String noteDescription;

    // Standard-Konstruktor
    public NoteForm() {}

    // Getter und Setter

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }
}