package com.udacity.jwdnd.course1.cloudstorage.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;

    public HomeController(
        FileService fileService, 
        NoteService noteService, 
        CredentialService credentialService
    ) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }


    private void loadModelForUser(int userId, Model model) {
        var credentials = credentialService.getAllCredentialsForUser(userId);
        var passwords = credentials.stream()
            .collect(Collectors.toMap(
                Credential::getUserid,
                credential -> credentialService.decryptPassword(credential)
            ));

        model.addAttribute("files", fileService.getFilesForUser(userId)); 
        model.addAttribute("notes", noteService.getAllNotesForUser(userId));
        model.addAttribute("credentials", credentials);
        model.addAttribute("passwords", passwords);
    }

    @GetMapping()
    public String getHomePage(Authentication authentication, Model model) {
        var user = (User)authentication.getPrincipal();
        Integer userId = user.getUserId();
        loadModelForUser(userId, model);
        return "home";
    }


    @PostMapping(params="action=uploadFile")
    public String handleFileUpload(
        @RequestParam("fileUpload") MultipartFile file,
        Authentication authentication, 
        Model model
    ) {
        var user = (User)authentication.getPrincipal();
        Integer currentUserId = user.getUserId();
        loadModelForUser(currentUserId, model);
  
        // 2. Basic validation for empty file
        if (file.isEmpty() || file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            model.addAttribute("uploadError", "Please select a file to upload.");
            // Return "home" directly to display the error message without redirecting
            return "home"; 
        }
        
        try {
            // 3. Delegate the business logic to the service layer
            fileService.saveFile(file, currentUserId);
            
            // 4. Success: Use PRG pattern to prevent resubmission.
            // NOTE: A successful message CANNOT be added to the Model here because we are redirecting.
            // The success must be checked in the GET method, possibly via a query parameter, 
            // but we will keep it simple and just redirect.
            
        } catch (IllegalArgumentException e) {
            // Handle validation errors (e.g., file name already exists)
            model.addAttribute("uploadError", e.getMessage());
            return "home"; // Stay on "home" to show error
        } catch (IOException e) {
            // Handle I/O errors
            model.addAttribute("uploadError", 
                "An I/O error occurred while processing the file: " + e.getMessage());
            return "home"; // Stay on "home" to show error
        } catch (Exception e) {
            // Handle all other unexpected errors
            model.addAttribute("uploadError", 
                "An unexpected error occurred: " + e.getMessage());
            return "home"; // Stay on "home" to show error
        }

        // 5. Success Redirect: Prevents form resubmission (PRG pattern)
        return "redirect:/home";
    }

    
    /**
     * Handles the file view/download request.
     * Fetches the complete file (including binary data) and returns it as a downloadable resource.
     * * @param fileId The ID of the file to be viewed/downloaded, passed as a path variable.
     * @return ResponseEntity containing the file data and necessary headers.
     */
    @GetMapping("/view/{fileId}")
    public ResponseEntity<ByteArrayResource> viewFile(@PathVariable Integer fileId) {
        
        File file = fileService.getFile(fileId);
        
        if (file == null) {
            // Return 404 Not Found if the file doesn't exist
            return ResponseEntity.notFound().build();
        }

        // 1. Prepare the resource (the file data)
        ByteArrayResource resource = new ByteArrayResource(file.getFiledata());

        // 2. Build the HTTP response headers
        return ResponseEntity.ok()
                // Set the correct Content-Type (MIME type)
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                // Set the Content-Disposition header to prompt download
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + file.getFilename() + "\"")
                // Set the content length
                .contentLength(Long.parseLong(file.getFilesize()))
                // Set the body of the response to the file data
                .body(resource);
    }

    // --- Implementation of Delete ---

    /**
     * Handles the file deletion request (POST method is used for security).
     * @param fileId The ID of the file to be deleted, passed as a path variable.
     * @param redirectAttributes Used to pass a flash message back to the home page.
     * @return Redirect to the home page.
     */
    @PostMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, RedirectAttributes redirectAttributes) {
        
        int rowsDeleted = fileService.deleteFile(fileId);
        
        if (rowsDeleted > 0) {
            redirectAttributes.addFlashAttribute("uploadSuccess", "File was successfully deleted.");
        } else {
            redirectAttributes.addFlashAttribute("uploadError", "File could not be found or deleted.");
        }
        
        // Use PRG pattern (Post/Redirect/Get)
        return "redirect:/home";
    }





    @PostMapping(params="action=saveNote")
    public String handleNoteSubmit(
        @ModelAttribute NoteForm noteForm, 
        Authentication authentication,
        Model model
    ) {
        var user = (User)authentication.getPrincipal();
        Integer userId = user.getUserId();

        var note = new Note();
        note.setNoteid(noteForm.getNoteId()); 
        note.setNotetitle(noteForm.getNoteTitle());
        note.setNotedescription(noteForm.getNoteDescription());
        note.setUserid(userId);

        try {
            int rowsAffected = noteService.saveOrUpdateNote(note);
            
            if (rowsAffected == 0) {
                // This means validation failed inside the service (e.g., title empty)
                model.addAttribute("noteError", "Could not save the note. Title is required.");
                return "home";
            }

        } catch (Exception e) {
            // Catch unexpected errors (e.g., database connection issues)
            System.err.println("Error saving/updating note: " + e.getMessage());
            model.addAttribute("noteError", "An unexpected error occurred while saving the note: " + e.getMessage());
            return "home";
        }

        // 4. Success: Use PRG Pattern. 
        // NOTE: No success message is displayed as Model attributes are dropped upon redirect.
        return "redirect:/home";
    }


/**
     * Handles the note deletion request using the POST method for security.
     * Deletes the note record from the database based on the path variable 'noteId'.
     *
     * @param noteId The ID of the note to be deleted.
     * @param redirectAttributes Used to pass a flash message back to the home page (PRG pattern).
     * @return Redirect to the home page.
     */
    @PostMapping("/note/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, RedirectAttributes redirectAttributes) {
        
        try {
            int rowsDeleted = noteService.deleteNote(noteId);
            
            if (rowsDeleted > 0) {
                // Success message
                redirectAttributes.addFlashAttribute("noteSuccess", "Note with ID " + noteId + " was successfully deleted.");
            } else {
                // File not found or not deleted (0 rows affected)
                redirectAttributes.addFlashAttribute("noteError", "Could not find or delete the note (ID: " + noteId + ").");
            }
        } catch (Exception e) {
            // Handle unexpected database errors
            System.err.println("Error deleting note: " + e.getMessage());
            redirectAttributes.addFlashAttribute("noteError", "An unexpected error occurred while deleting the note.");
        }
        
        // Use PRG pattern (Post/Redirect/Get) to return to the list view
        return "redirect:/home";
    }    


/**
     * Handles the submission of the credential form (save or update).
     *
     * @param credentialForm The form data mapped to a CredentialForm object.
     * @param authentication The current user's session details for the userId.
     * @param redirectAttributes Used for passing messages across the redirect (PRG).
     * @param model Model, unused for success, but part of prior signature requirements.
     * @return Redirect to the home page.
     */
    @PostMapping(params="action=saveCredential")
    public String handleCredentialSubmit(
        @ModelAttribute CredentialForm credentialForm, 
        Authentication authentication, 
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        var user = (User)authentication.getPrincipal();
        Integer userId = user.getUserId();
        
        // 1. Map form data to the model entity (Credential)
        var credential = new Credential();
        credential.setCredentialid(credentialForm.getCredentialId()); 
        credential.setUrl(credentialForm.getUrl());
        credential.setUsername(credentialForm.getUsername());
        
        // IMPORTANT: The plaintext password is submitted here. The Service will handle encryption.
        credential.setPassword(credentialForm.getPassword()); 
        credential.setUserid(userId); 

        String successMessage;

        try {
            // 2. Delegate business logic (save or update, including encryption) to the service
            int rowsAffected = credentialService.saveOrUpdateCredential(credential);
            
            if (rowsAffected > 0) {
                if (credentialForm.getCredentialId() != null) {
                    successMessage = "Credential for '" + credentialForm.getUrl() + "' was updated successfully!";
                } else {
                    successMessage = "Credential for '" + credentialForm.getUrl() + "' was saved successfully!";
                }
                redirectAttributes.addFlashAttribute("credentialSuccess", successMessage);
            } else {
                redirectAttributes.addFlashAttribute("credentialError", "Could not save the credential. Check required fields.");
            }

        } catch (Exception e) {
            System.err.println("Error saving/updating credential: " + e.getMessage());
            redirectAttributes.addFlashAttribute("credentialError", "An unexpected error occurred while saving the credential.");
        }

        // 3. PRG Pattern: Redirect
        return "redirect:/home";
    }

    // --- Implementation of Delete Credential ---

    /**
     * Handles the credential deletion request using the POST method.
     *
     * @param credentialId The ID of the credential to be deleted.
     * @param redirectAttributes Used to pass a flash message back to the home page (PRG pattern).
     * @return Redirect to the home page.
     */
    @PostMapping("/credential/delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, RedirectAttributes redirectAttributes) {
        
        try {
            int rowsDeleted = credentialService.deleteCredential(credentialId);
            
            if (rowsDeleted > 0) {
                redirectAttributes.addFlashAttribute("credentialSuccess", "Credential was successfully deleted.");
            } else {
                redirectAttributes.addFlashAttribute("credentialError", "Could not find or delete the credential.");
            }
        } catch (Exception e) {
            System.err.println("Error deleting credential: " + e.getMessage());
            redirectAttributes.addFlashAttribute("credentialError", "An unexpected error occurred while deleting the credential.");
        }
        
        // PRG Pattern
        return "redirect:/home";
    }

    // (Optional) Standard-POST-Methode oder Fehlerbehandlung
    @PostMapping
    public String handleUnknownPost() {
        // Diese Methode würde ausgelöst, wenn ein POST an /home gesendet wird, 
        // der keinem der spezifischen `params`-Werte entspricht.
        System.out.println("LOGIC: Unbekannter POST-Submit.");
        return "redirect:/home";
    }

}
