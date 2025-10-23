package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credential {
    
    private Integer credentialid;
    private String url;
    private String username;
    private String salt;
    private String password;
    private Integer userid;
}
