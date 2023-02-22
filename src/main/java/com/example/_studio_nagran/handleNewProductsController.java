package com.example._studio_nagran;

import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class handleNewProductsController {

    private final DatabaseController conn = new DatabaseController();

     protected boolean InsertAuthor(String authorName, File image){

         if(!this.CheckIfAuthorExists(authorName)){
             return false;
         }
         InputStream fs = null;
         if(image != null) {
             try {
                 fs = new FileInputStream(image);
             } catch (FileNotFoundException ex) {
                 System.err.println(ex);
             }
         }
         int result = 0;
             try (Connection connDB = this.conn.getConnection()) {
                 String insert = "INSERT INTO authors(authorName, authorAvatar) VALUES (?, ?)";
                 PreparedStatement query = connDB.prepareStatement(insert);
                query.setString(1, authorName);
                 query.setBlob(2, fs);
                 result = query.executeUpdate();
                 if(result == 1){
                     return true;
                 }
             }catch (SQLException e) {
                 System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
             } catch (Exception e) {
                 e.printStackTrace();
             }
            return false;
        }
     private boolean CheckIfAuthorExists(String authorName){
         int result = 0;
         try (Connection connDB = conn.getConnection()) {
             String check = "SELECT count(*) AS total FROM authors WHERE authorName= ?";
             PreparedStatement query = connDB.prepareStatement(check);
             query.setString(1, authorName);

             ResultSet rs = query.executeQuery();

             while (rs.next()) {
                 result = rs.getInt("total");
             }
             if (result == 0) return true;
         }catch (SQLException e) {
             System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
         } catch (Exception e) {
             e.printStackTrace();
         }
         return false;
     }
     protected boolean InsertDisk(String diskName, File image){
            if(!this.CheckIfDiskExists(diskName)){
                return false;
            }
            InputStream fs = null;
         if(image != null) {
             try {
                 fs = new FileInputStream(image);
             } catch (FileNotFoundException ex) {
                 System.err.println(ex);
             }
         }
            int result = 0;
            try (Connection connDB = this.conn.getConnection()) {
                String insert = "INSERT INTO disks(diskName, diskAvatar) VALUES (?, ?)";
                PreparedStatement query = connDB.prepareStatement(insert);
                query.setString(1, diskName);
                query.setBlob(2, fs);
                result = query.executeUpdate();
                if(result == 1){
                    return true;
                }
            }catch (SQLException e) {
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        protected boolean InsertSongs(String songName, File image, String authorName, String diskName){

            if(!this.CheckIfSongExists(songName)) {
            return false;
            }

            if(!this.CheckIfAuthorExists(authorName)){
                this.InsertAuthor(authorName, null);
            }
            int authorID = this.fetchAuthorsID(authorName);
            InputStream fs = null;
            if(image != null) {
                try {
                    fs = new FileInputStream(image);
                } catch (FileNotFoundException ex) {
                    System.err.println(ex);
                }
            }
            int result = 0;
            try (Connection connDB = this.conn.getConnection()) {
                String insert = "INSERT INTO songs(songName, songAvatar, authorsID) VALUES (?, ?, ?)";
                PreparedStatement query = connDB.prepareStatement(insert);
                query.setString(1, songName);
                query.setBlob(2, fs);
                query.setInt(3,authorID);
                result = query.executeUpdate();
                if(result == 1){
                    if(!diskName.isEmpty()){
                        this.InsertSongtoDiskRelation(this.fetchDiskID(diskName), this.fetchSongID(songName));
                    }
                    return true;
                }
            }catch (SQLException e) {
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
     private boolean CheckIfDiskExists(String diskName){
         int result = 0;
         try (Connection connDB = conn.getConnection()) {
             String check = "SELECT count(*) AS total FROM disks WHERE diskName= ?";
             PreparedStatement query = connDB.prepareStatement(check);
             query.setString(1, diskName);

             ResultSet rs = query.executeQuery();

             while (rs.next()) {
                 result = rs.getInt("total");
             }
             if (result == 0) return true;
         }catch (SQLException e) {
             System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
         } catch (Exception e) {
             e.printStackTrace();
         }
         return false;
     }
     private boolean CheckIfSongExists(String songName){
         int result = 0;
         try (Connection connDB = conn.getConnection()) {
             String check = "SELECT count(*) AS total FROM songs WHERE songName= ?";
             PreparedStatement query = connDB.prepareStatement(check);
             query.setString(1, songName);

             ResultSet rs = query.executeQuery();

             while (rs.next()) {
                 result = rs.getInt("total");
             }
             if (result == 0) return true;
         }catch (SQLException e) {
             System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
         } catch (Exception e) {
             e.printStackTrace();
         }
         return false;
     }
     private int fetchAuthorsID(String authorName){
         int result = 0;
         try (Connection connDB = conn.getConnection()) {
             String check = "SELECT IDauthor AS id FROM authors WHERE authorName= ?";
             PreparedStatement query = connDB.prepareStatement(check);
             query.setString(1, authorName);

             ResultSet rs = query.executeQuery();
             System.out.println("RS"+rs);

             while (rs.next()) {
                result = rs.getInt("id");
             }
         }catch (SQLException e) {
        System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
    }
        return result;
     }
    private int fetchDiskID(String diskName){
        int result = 0;
        try (Connection connDB = conn.getConnection()) {
            String check = "SELECT diskID AS id FROM disks WHERE diskName= ? ";
            PreparedStatement query = connDB.prepareStatement(check);
            query.setString(1, diskName);

            ResultSet rs = query.executeQuery();
            System.out.println("RS"+rs);

            while (rs.next()) {
                result = rs.getInt("id");
            }
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private int fetchSongID(String songName){
        int result = 0;
        try (Connection connDB = conn.getConnection()) {
            String check = "SELECT songID AS id FROM songs WHERE songName= ? LIMIT 1" ;
            PreparedStatement query = connDB.prepareStatement(check);
            query.setString(1, songName);

            ResultSet rs = query.executeQuery();
            System.out.println("RS"+rs);

            while (rs.next()) {
                result = rs.getInt("id");
            }
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private boolean InsertSongtoDiskRelation(int diskID, int songID){
        int result = 0;
        try (Connection connDB = this.conn.getConnection()) {
            String insert = "INSERT INTO songstodisksrelation(diskID, songID) VALUES (?, ?)";
            PreparedStatement query = connDB.prepareStatement(insert);
            query.setInt(1, diskID);
            query.setInt(2, songID);
            result = query.executeUpdate();
            if(result == 1){
                return true;
            }
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
