package com.example.mysqlserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "t_coric_FileCopyProperties")
public class TCoricFilecopyproperty {
    @Column(name = "coric_ext_id", nullable = false)
    private Integer coricExtId;

    @Column(name = "ExcecutionID", length = 15)
    private String excecutionID;

    @Column(name = "ExcecutionTime")
    private Instant excecutionTime;

    @Column(name = "RequestID")
    private Integer requestID;

    @Column(name = "FolderYear", length = 4)
    private String folderYear;

    public String getFolderYear() {
        return folderYear;
    }

    public void setFolderYear(String folderYear) {
        this.folderYear = folderYear;
    }

    public Integer getRequestID() {
        return requestID;
    }

    public void setRequestID(Integer requestID) {
        this.requestID = requestID;
    }

    public Instant getExcecutionTime() {
        return excecutionTime;
    }

    public void setExcecutionTime(Instant excecutionTime) {
        this.excecutionTime = excecutionTime;
    }

    public String getExcecutionID() {
        return excecutionID;
    }

    public void setExcecutionID(String excecutionID) {
        this.excecutionID = excecutionID;
    }

    public Integer getCoricExtId() {
        return coricExtId;
    }

    public void setCoricExtId(Integer coricExtId) {
        this.coricExtId = coricExtId;
    }
}