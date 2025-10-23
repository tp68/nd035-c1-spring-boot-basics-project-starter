package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.Data;

@Data
public class File {
    private Integer fileId;
    private String filename;
    private String contenttype;
    private String filesize; // Stored as VARCHAR in the DB
    private Integer userid;
    private byte[] filedata; // Stored as BYTEA in the DB for binary data
}
