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


    /*
    * Metoda odpowiedzialna za wyciągnięcie Autorów z bazy danych
     */
    public static ObservableList<Author> retrieveAuthors(){

        // określam Liste obserwowalną Autorów.
        ObservableList<Author> authors = FXCollections.observableArrayList();
        // nawiązuje połączenie z bazą danych.
        try (Connection connDB = conn.getConnection()) {


            // przygotowuję i wykonuje zapytanie do bazy danych.
            String insert = "SELECT * from authors";
            PreparedStatement query = connDB.prepareStatement(insert);
            ResultSet rs = query.executeQuery();

            // przechodzę po otrzymanych wynikach.
            while(rs.next()){
                // sprawdzam czy do Autora przypisany jest obrazek w postaci BLOB
                if(rs.getBlob("authorAvatar") != null) {
                    // dodaje nowych autorów do listy
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
        // zwracam autorów
        return authors;
    }

    /*
    *Metoda odpowiedzialna za wyciągnięcie Utworów z Bazy Danych
    *
    *
     */
    public static ObservableList<Song> retrieveSongs(){
        // definiuje songs jako listę obserwowaną
        ObservableList<Song> songs = FXCollections.observableArrayList();
            // nawiązuje połączenie z bazą.
        try (Connection connDB = conn.getConnection()) {



            String insert = "SELECT * from songs INNER JOIN authors ON songs.authorsID = authors.IDauthor INNER JOIN disks ON songs.songID";
            PreparedStatement query = connDB.prepareStatement(insert);
            ResultSet rs = query.executeQuery();

            while(rs.next()){
                // przechodzę po otrzymanych wynikach i sprawdzam czy grafika Utworu jest przypisana do danego rekordu, jeżeli nie tworzę obiekt bez Avatara.
                if(rs.getBlob("songAvatar") != null) {
                    songs.add(new Song(rs.getInt("songID"), rs.getString("songName"), rs.getString("authorname"), rs.getString("diskName"), new Image(rs.getBlob("songAvatar").getBinaryStream())));
                }else{
                    songs.add(new Song( rs.getInt("songID"), rs.getString("songName"), rs.getString("authorname"), rs.getString("diskName"),null) );

                }
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
                if(rs.getBlob("diskAvatar")!=null) {
                    disks.add(new Disk(rs.getInt("diskID"), rs.getString("diskName"), new Image(rs.getBlob("diskAvatar").getBinaryStream())));
                }
                else{
                    disks.add(new Disk( rs.getInt("diskID"), rs.getString("diskName"), null));

                }
                }
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return disks;
    }
}
