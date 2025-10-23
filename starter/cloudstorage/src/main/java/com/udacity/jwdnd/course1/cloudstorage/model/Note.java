package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.Data; 

@Data
public class Note {
    private Integer noteid;          // noteid (serial PRIMARY KEY)
    private String notetitle;        // notetitle (VARCHAR(20))
    private String notedescription;  // notedescription (VARCHAR(1000))
    private Integer userid;          // userid (INT, foreign key)
}
