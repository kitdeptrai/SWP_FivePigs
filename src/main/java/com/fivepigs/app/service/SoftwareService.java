/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.service;

/**
 *
 * @author MinhPD
 */
import com.fivepigs.app.config.Db;
import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.model.Software;
import jakarta.servlet.http.Part;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class SoftwareService {

    private final SoftwareDao dao;

    public SoftwareService() {
        this.dao = new SoftwareDao();
    }

    public int createSoftware(Software software) throws Exception {
        return dao.createSoftware(software);
    }

    public void addSoftwareGenres(int softwareId, String[] genreIds) throws Exception {
        SoftwareDao dao = new SoftwareDao();
        dao.insertSoftwareGenres(softwareId, genreIds);
    }

    public void addSoftwareVersion(
            int softwareId,
            String versionName,
            String fileUrl,
            String releaseNote,
            long fileSize
    ) throws Exception {

        dao.addSoftwareVersion(
                softwareId,
                versionName,
                fileUrl,
                releaseNote,
                fileSize
        );
    }

    public void addSoftwareImage(int softwareId, String imageUrl, boolean isThumbnail) throws Exception {
        dao.addSoftwareImage(softwareId, imageUrl, isThumbnail);
    }

    public String validateUpload(String name, String description, String price, String category) {

        if (name == null || name.trim().length() < 2 || name.trim().length() > 100) {
            return "Software name must be 2-100 characters";
        }

        if (description == null || description.trim().length() < 5) {
            return "Description is too short";
        }

        if (price == null || price.trim().isEmpty()) {
            return "Price must not be empty";
        }

        try {
            double p = Double.parseDouble(price);
            if (p < 0) {
                return "Invalid price";
            }
        } catch (NumberFormatException e) {
            return "Price must be a number";
        }

        if (category == null || category.trim().isEmpty()) {
            return "You must select a category";
        }

        return null;
    }

    public void createSoftwareDetail(
            int softwareId,
            String description,
            String systemRequire,
            String releaseNote
    ) throws Exception {

        dao.createSoftwareDetail(
                softwareId,
                description,
                systemRequire,
                releaseNote
        );
    }

    public String validateFiles(Part softwareFile, Part thumbnail) {

        if (softwareFile == null || softwareFile.getSize() == 0) {
            return "Ban phai upload file software (.txt)";
        }

        String fileName = softwareFile.getSubmittedFileName();

        if (!fileName.toLowerCase().endsWith(".txt")) {
            return "Software phai la file .txt";
        }

        if (thumbnail == null || thumbnail.getSize() == 0) {
            return "Ban phai upload thumbnail";
        }

        String thumbName = thumbnail.getSubmittedFileName().toLowerCase();

        if (!(thumbName.endsWith(".png") || thumbName.endsWith(".jpg") || thumbName.endsWith(".jpeg"))) {
            return "Thumbnail phai la anh (.png .jpg .jpeg)";
        }

        return null;
    }

    public String validateFiles(Part softwareFile) {

        if (softwareFile == null || softwareFile.getSize() == 0) {
            return "Ban phai upload file software (.txt)";
        }

        String fileName = softwareFile.getSubmittedFileName();

        if (!fileName.toLowerCase().endsWith(".txt")) {
            return "Software phai la file .txt";
        }

        return null;
    }

    public void resubmitSoftware(
            int softwareId,
            String name,
            String shortDescription,
            int categoryId,
            double price,
            String description,
            String systemRequire,
            String releaseNote
    ) throws Exception {

        dao.updateSoftware(
                softwareId,
                name,
                shortDescription,
                categoryId,
                price
        );

        dao.updateSoftwareDetail(
                softwareId,
                description,
                systemRequire,
                releaseNote
        );
    }
}
