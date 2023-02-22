package com.example._studio_nagran;

import java.sql.*;

public class DatabaseController {
        private String url;
    public Connection dbLink;

    public Connection getConnection(){
         String dbName ="sql11592473";
      String dbUser ="sql11592473";
        String dbPassword ="SZ4ZjfVf1d";
          url = "jdbc:mysql://sql11.freemysqlhosting.net/"+dbName;


         try{

                Class.forName("com.mysql.cj.jdbc.Driver");
                dbLink = DriverManager.getConnection(url, dbUser, dbPassword);
            }catch(Exception e){
             e.printStackTrace();
         }
        return dbLink;
    }



}
