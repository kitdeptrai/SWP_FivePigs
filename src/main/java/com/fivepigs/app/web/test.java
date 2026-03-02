/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fivepigs.app.web;

import com.fivepigs.app.dao.SoftwareDao;
import com.fivepigs.app.dao.VendorDao;
import com.fivepigs.app.model.Software;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MinhPD
 */
public class test {

    public static void main(String[] args) {
        try{
        SoftwareDao swdao = new SoftwareDao();
        
        List<Software> sw = swdao.Top3RevenueByVendor(2);
        for (Software s : sw) {
            System.out.println(s.getSoftwareId());
        }
        }catch(SQLException e){
            System.out.println("loi");
        }
    }
}
