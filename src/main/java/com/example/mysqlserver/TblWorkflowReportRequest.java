package com.example.mysqlserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "tblWorkflowReportRequest")
public class TblWorkflowReportRequest {
    @Id
    @Column(name = "WorkflowReportRequestID", nullable = false)
    private Long workflowReportRequestID;

    @Column(name = "WorkflowReportID", length = 15)
    private String workflowReportID;

    @Column(name = "ReportName", length = 225)
    private String reportName;

    @Column(name = "ReportCreatedAt")
    private Instant reportCreatedAt;

    @Column(name = "RequestCreatedAt")
    private Instant requestCreatedAt;

    @Lob
    @Column(name = "MetaDataFileName")
    private String metaDataFileName;

    @Column(name = "MultiClientGroupID")
    private Long multiClientGroupID;

    public Long getMultiClientGroupID() {
        return multiClientGroupID;
    }

    public void setMultiClientGroupID(Long multiClientGroupID) {
        this.multiClientGroupID = multiClientGroupID;
    }

    public String getMetaDataFileName() {
        return metaDataFileName;
    }

    public void setMetaDataFileName(String metaDataFileName) {
        this.metaDataFileName = metaDataFileName;
    }

    public Instant getRequestCreatedAt() {
        return requestCreatedAt;
    }

    public void setRequestCreatedAt(Instant requestCreatedAt) {
        this.requestCreatedAt = requestCreatedAt;
    }

    public Instant getReportCreatedAt() {
        return reportCreatedAt;
    }

    public void setReportCreatedAt(Instant reportCreatedAt) {
        this.reportCreatedAt = reportCreatedAt;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getWorkflowReportID() {
        return workflowReportID;
    }

    public void setWorkflowReportID(String workflowReportID) {
        this.workflowReportID = workflowReportID;
    }

    public Long getWorkflowReportRequestID() {
        return workflowReportRequestID;
    }

    public void setWorkflowReportRequestID(Long workflowReportRequestID) {
        this.workflowReportRequestID = workflowReportRequestID;
    }
}