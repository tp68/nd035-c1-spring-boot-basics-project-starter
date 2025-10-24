package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Global Exception Handler to catch file upload errors (especially size limits) 
 * and redirect the user in a friendly manner.
 */
@ControllerAdvice
public class FileUploadExceptionHandler {

    // Catches MaxUploadSizeExceededException, which is thrown when the file is too large
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
        // Add an error message to be displayed on the target page
        redirectAttributes.addFlashAttribute("uploadError", "Upload error: The file is too large! The maximum size is 1MB.");

        // Redirect the user back to the Home page. 
        // Adjust the path if you have a different error page.
        return "redirect:/home"; 
    }
}
