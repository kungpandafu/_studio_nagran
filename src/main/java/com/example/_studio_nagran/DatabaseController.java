package com.example._studio_nagran;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseController {

    public Connection dbLink;

    public Connection getConnection(){
         String dbName ="projekt";
      String dbUser ="root";
        String dbPassword ="";

        String url ="jdbc:mysql://localhost:3306/"+ dbName;

         try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                dbLink = DriverManager.getConnection(url, dbUser, dbPassword);
            }catch(Exception e){
             e.printStackTrace();
         }
        return dbLink;
    }

}
