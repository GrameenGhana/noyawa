package org.motechproject.noyawa.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class dbConnection {
	
	public Connection noYawaConnection() throws Exception{
	Context initCtx = new InitialContext();
        DataSource ds = (DataSource) initCtx.lookup("java:/comp/env/jdbc/NoyawaDB"); // commented by Liman
                
//          Connection  connection = DriverManager
//                .getConnection("jdbc:mysql://localhost:3306/noyawa", "noyawa", "noyawa");
//                
        return ds.getConnection();  // commented by Liman
          
          //return connection;
          
	}
}
