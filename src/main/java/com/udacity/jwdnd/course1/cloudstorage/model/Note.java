package com.udacity.jwdnd.course1.cloudstorage.model;

import javax.persistence.Column;
import javax.validation.constraints.Size;

public class Note {

    private Integer noteid;
    private String notetitle;
    @Column(length = 1000)
    private String notedescription;
    private Integer userid;

    public Note() {}

    public Note(String notetitle, String notedescription, Integer userid) {
        this.notetitle = notetitle;
        this.notedescription = notedescription;
        this.userid = userid;
    }

    public Note(Integer noteid, String notetitle, String notedescription, Integer userid) {
        this.noteid = noteid;
        this.notetitle = notetitle;
        this.notedescription = notedescription;
        this.userid = userid;
    }

    public Integer getNoteid() {
        return noteid;
    }

    public void setNoteid(Integer noteid) {
        this.noteid = noteid;
    }

    public String getNotetitle() {
        return notetitle;
    }

    public void setNotetitle(String notetitle) {
        this.notetitle = notetitle;
    }

    public String getNotedescription() {
        return notedescription;
    }

    public void setNotedescription(String notedescription) {
        this.notedescription = notedescription;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
