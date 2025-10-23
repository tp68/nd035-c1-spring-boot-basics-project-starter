package com.udacity.jwdnd.course1.cloudstorage.model;

//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;

public class CredentialForm {

    // Muss Integer sein, um 'null' zu akzeptieren, wenn es sich um eine NEUE Credential handelt.
    private Integer credentialId;

    //@NotBlank(message = "URL cannot be blank")
    //@Size(max = 100, message = "URL must be 100 characters or less")
    private String url;

    //@NotBlank(message = "Username cannot be blank")
    //@Size(max = 30, message = "Username must be 30 characters or less")
    private String username;

    //@NotBlank(message = "Password cannot be blank")
    //@Size(max = 30, message = "Password must be 30 characters or less")
    private String password;

    // Standard-Konstruktor
    public CredentialForm() {}

    // Getter und Setter

    public Integer getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(Integer credentialId) {
        this.credentialId = credentialId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
