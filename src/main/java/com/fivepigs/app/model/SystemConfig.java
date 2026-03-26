/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.model;

/**
 *
 * @author MinhPD
 */
public class SystemConfig {
//    CREATE TABLE System_Config (
//    config_key VARCHAR(50) PRIMARY KEY,
//    config_value VARCHAR(100)
//);
    private String configKey;
    private String configValue;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
    
    
}
