/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.motechproject.noyawa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.motechproject.noyawa.constant.YawaConstant;

/**
 *
 * @author liman
 */
public class SMSDao {
    
    private dbConnection yawaConnection = new dbConnection();
    private YawaConstant yawaConstant = new YawaConstant();
    
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
	
	public void logSms(String phone_number , String message ,String status) throws Exception{
		
		connection = yawaConnection.noYawaConnection();
		preparedStatement = connection.prepareStatement(yawaConstant.smsLogInsert);
		preparedStatement.setString(1, "OUTBOUND");
        preparedStatement.setString(2, "7005");
        preparedStatement.setString(3, phone_number.trim());
        preparedStatement.setString(4, message);
        preparedStatement.setString(5, "system");
        preparedStatement.setString(6, status);
        Date dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime =sdf.format(dt);
        
        preparedStatement.setString(7, currentTime);
        preparedStatement.executeUpdate(); 
        
        checkConnection();
	}
        
        private void checkConnection() throws Exception{
		if (preparedStatement != null) {
            preparedStatement.close();
        }

        if (connection != null) {
            connection.close();
        }	
	}
}
