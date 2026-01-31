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
    private int detailId,softwareId;
    private String description,version,systemRequirement,releaseNote;

    public SoftwareDetail() {
    }

    public SoftwareDetail(int detailId, int softwareId, String description, String version, String systemRequirement, String releaseNote) {
        this.detailId = detailId;
        this.softwareId = softwareId;
        this.description = description;
        this.version = version;
        this.systemRequirement = systemRequirement;
        this.releaseNote = releaseNote;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(int softwareId) {
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

    public String getSystemRequirement() {
        return systemRequirement;
    }

    public void setSystemRequirement(String systemRequirement) {
        this.systemRequirement = systemRequirement;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }
    
    
}
