package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService; 

    // Constructor Injection
    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    /**
     * Retrieves all credentials for a specific user.
     * @param userId The ID of the currently logged-in user.
     * @return A list of Credential objects (with encrypted passwords).
     */
    public List<Credential> getAllCredentialsForUser(Integer userId) {
        // NOTE: The service returns the credentials as stored in the DB (encrypted).
        return credentialMapper.getCredentialsForUser(userId);
    }

    /**
     * Saves or updates a credential. Handles the encryption of the password.
     *
     * @param credential The Credential object containing form data.
     * @return The number of affected rows (1 on success).
     */
    public int saveOrUpdateCredential(Credential credential) {
        // 1. Generate a new encryption key (key) for the password (plaintext password comes from the form/model).
        String key = generateKey();
        
        // 2. Encrypt the password using the encryption service.
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);

        // 3. Set the generated key and the encrypted password on the model.
        credential.setSalt(key);
        credential.setPassword(encryptedPassword);

        // 4. Decide whether to insert or update.
        if (credential.getCredentialid() != null) {
            // Update existing credential
            return credentialMapper.updateCredential(credential);
        } else {
            // Insert new credential
            return credentialMapper.insertCredential(credential);
        }
    }
    
    /**
     * Deletes a credential by its ID.
     * @param credentialId The ID of the credential to delete.
     * @return The number of rows deleted.
     */
    public int deleteCredential(Integer credentialId) {
        return credentialMapper.deleteCredential(credentialId);
    }
    
    /**
     * Decrypts the password for a given credential object.
     * NOTE: This method should typically only be called right before displaying or 
     * using the password (e.g., in a secure modal).
     *
     * @param credential The Credential object containing the encrypted password and key.
     * @return The decrypted password as a String.
     */
    public String decryptPassword(Credential credential) {
        if (credential == null || credential.getPassword() == null || credential.getSalt() == null) {
            return "";
        }
        return encryptionService.decryptValue(credential.getPassword(), credential.getSalt());
    }

    // --- Helper Method ---

    /**
     * Generates a random key for encryption (salt).
     * @return A Base64 encoded random String.
     */
    private String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
