/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.web;

import com.fivepigs.app.dao.VendorDao;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author MinhPD
 */
public class test {
    
    public static void main(String[] args) {
        try {
            VendorDao vddao = new VendorDao();
            Integer sumApprovedApps = vddao.sumApprovedApps(2);
            Map<Integer, Double> revenueMap = vddao.revenueMap(2);
            for(int i=1;i<=4;i++){
                System.out.println(revenueMap.get(i));
            }
            System.out.println("Approved apps = " + sumApprovedApps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

