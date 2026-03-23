/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

/**
 *
 * @author MinhPD
 */
public class SoftwareDetail {
//    detail_id INT IDENTITY(1,1) PRIMARY KEY,
//software_id INT UNIQUE,
//description NVARCHAR(MAX),
//version VARCHAR(50),
//system_requirement NVARCHAR(MAX),
//release_note NVARCHAR(MAX),

    private Integer detailId;
    private Integer softwareId;
    private String description;
    private String version;
    private String sysRequirement;
    private String releaseNote;
    private String language;

    public SoftwareDetail() {
    }

    public SoftwareDetail(Integer detailId, Integer softwareId, String description, String version, String sysRequirement, String releaseNote, String language) {
        this.detailId = detailId;
        this.softwareId = softwareId;
        this.description = description;
        this.version = version;
        this.sysRequirement = sysRequirement;
        this.releaseNote = releaseNote;
        this.language = language;
    }

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public Integer getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSysRequirement() {
        return sysRequirement;
    }

    public void setSysRequirement(String sysRequirement) {
        this.sysRequirement = sysRequirement;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
