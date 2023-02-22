package com.example._studio_nagran;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class retrieveProductsController {
    private final static DatabaseController conn = new DatabaseController();
    public static ObservableList<Author> retrieveAuthors(){

        ObservableList<Author> authors = FXCollections.observableArrayList();
        try (Connection connDB = conn.getConnection()) {



            String insert = "SELECT * from authors";
            PreparedStatement query = connDB.prepareStatement(insert);
            ResultSet rs = query.executeQuery();

            while(rs.next()){
                if(rs.getBlob("authorAvatar") != null) {
                    authors.add(new Author(rs.getInt("IDauthor"), rs.getString("authorName"), new Image(rs.getBlob("authorAvatar").getBinaryStream())));
                }
                else{
                    authors.add(new Author(rs.getInt("IDauthor"), rs.getString("authorName"), null));
                }
                }
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authors;
    }
    public static ObservableList<Song> retrieveSongs(){
        ObservableList<Song> songs = FXCollections.observableArrayList();
        try (Connection connDB = conn.getConnection()) {



            String insert = "SELECT * from songs INNER JOIN authors ON songs.authorsID = authors.IDauthor INNER JOIN disks ON songs.songID";
            PreparedStatement query = connDB.prepareStatement(insert);
            ResultSet rs = query.executeQuery();

            while(rs.next()){
                songs.add(new Song( rs.getInt("songID"), rs.getString("songName"), rs.getString("authorname"), rs.getString("diskName"),new Image(rs.getBlob("songAvatar").getBinaryStream())) );
            }
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }
    public static ObservableList<Disk> retrieveDisks(){
        ObservableList<Disk> disks = FXCollections.observableArrayList();
        try (Connection connDB = conn.getConnection()) {



            String insert = "SELECT * from disks";
            PreparedStatement query = connDB.prepareStatement(insert);
            ResultSet rs = query.executeQuery();

            while(rs.next()){
                disks.add(new Disk( rs.getInt("diskID"), rs.getString("diskName"), new Image(rs.getBlob("diskAvatar").getBinaryStream())));
            }
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return disks;
    }
}
