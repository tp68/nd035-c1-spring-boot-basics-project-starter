package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    // Dependency Injection
    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    /**
     * Converts a MultipartFile into a File model object and saves it to the database.
     * Handles file name validation against existing files for the user.
     *
     * @param multipartFile The file received from the web request.
     * @param userId The ID of the currently logged-in user.
     * @return The number of rows inserted (1 on success).
     * @throws IOException If there's an issue reading the file content.
     * @throws IllegalArgumentException If a file with the same name already exists for the user.
     */
    public int saveFile(MultipartFile multipartFile, Integer userId) throws IOException {
        
        // 1. Validation: Check if a file with the same name already exists for the user
        String fileName = multipartFile.getOriginalFilename();
        if (fileName != null && getFileByNameAndUser(fileName, userId) != null) {
            // Throwing an exception allows the controller to handle the user-friendly error message
            throw new IllegalArgumentException("A file with the name '" + fileName + "' already exists for user " + userId + ".");
        }

        // 2. Map the MultipartFile data to the database File model
        File file = new File();
        file.setFilename(fileName);
        file.setContenttype(multipartFile.getContentType());
        // Storing size as a string (VARCHAR) as per the FILES table definition
        file.setFilesize(String.valueOf(multipartFile.getSize())); 
        file.setUserid(userId);
        
        // 3. Convert binary file content to byte array (for BYTEA column)
        file.setFiledata(multipartFile.getBytes()); 

        // 4. Insert the record via the mapper and return the result
        return fileMapper.insert(file);
    }

    // --- Auxiliary/Helper Methods ---

    public File getFileByNameAndUser(String fileName, Integer userId) {
        return fileMapper.getFileByNameAndUser(fileName, userId);
    }
    
    public List<File> getFilesForUser(Integer userId) {
        return fileMapper.getFilesByUser(userId);
    }
    
    public int deleteFile(Integer fileId) {
        return fileMapper.deleteFileById(fileId);
    }
    
    public File getFile(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }
}
