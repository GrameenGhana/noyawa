/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.motechproject.noyawa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.motechproject.noyawa.dao.RegistrationDao;
import org.motechproject.noyawa.dao.dbConnection;
import org.motechproject.noyawa.service.SubscriberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author liman
 */
public class TestCase {
    
  
  
 public static void main(String[] args){
 
  new SubscriberServiceImpl().registerSubscriber("233263");
       
     
    }
}
