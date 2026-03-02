    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package com.fivepigs.app.model;

    import java.sql.Timestamp;

    public class ReviewGuidelineItem {

        private int itemId;
        private int guidelineId;
        private String itemText;
        private int sortOrder;
        private Timestamp createdAt;

        public ReviewGuidelineItem() {
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public int getGuidelineId() {
            return guidelineId;
        }

        public void setGuidelineId(int guidelineId) {
            this.guidelineId = guidelineId;
        }

        public String getItemText() {
            return itemText;
        }

        public void setItemText(String itemText) {
            this.itemText = itemText;
        }

        public int getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
        }

        public Timestamp getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
        }
    }
