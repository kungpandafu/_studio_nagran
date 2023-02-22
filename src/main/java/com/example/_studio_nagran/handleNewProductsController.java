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

    // inicjuje połączenie z bazą danych
    private final DatabaseController conn = new DatabaseController();

    // metoda odpowiedzialna za dodanie Autora do bazy danych
     protected boolean InsertAuthor(String authorName, File image){
            // sprawdzam czy autor istnieje - jeżeli tak - to zwracam fałsz.
         if(!this.CheckIfAuthorExists(authorName)){
             return false;
         }
         // definiuje inputStream do przetworzenia obrazka
         InputStream fs = null;
         if(image != null) {
             try {
                 // jeżeli jest zdefiniowany obrazek, to tworzę fileInputStream
                 fs = new FileInputStream(image);
             } catch (FileNotFoundException ex) {
                 System.err.println(ex);
             }
         }
         int result = 0;
         // nawiązuje połączenie z bazą danych
             try (Connection connDB = this.conn.getConnection()) {
                 // przygotowuję zapytanie
                 String insert = "INSERT INTO authors(authorName, authorAvatar) VALUES (?, ?)";
                 PreparedStatement query = connDB.prepareStatement(insert);
                 // binduję parametry, obrazek przechowuję w bazie jako BLOB.
                query.setString(1, authorName);
                 query.setBlob(2, fs);
                 // wykonuję zapytanie
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

        // metoda odpowiedzialna za sprawdzenie czy Autor już istnieje w bazie danych
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

     // metoda odpowiedzialna za dodanie Płyty do bazy danych
     protected boolean InsertDisk(String diskName, File image){
         // sprawdzam czy płyta już istnieje
            if(!this.CheckIfDiskExists(diskName)){
                return false;
            }
            // przetwarzam obrazek
            InputStream fs = null;
         if(image != null) {
             try {
                 fs = new FileInputStream(image);
             } catch (FileNotFoundException ex) {
                 System.err.println(ex);
             }
         }
            int result = 0;

         // nawiązuje połączenie z bazą danych, przygotowuję zapytanie a następnie binduje parametry i wykonuje zapytanie
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

        // metoda odpowiedzialna za dodanie utworu do bazy danych
        protected boolean InsertSongs(String songName, File image, String authorName, String diskName){
                // sprawdzam czy utwór już istnieje w bazie danych
            if(!this.CheckIfSongExists(songName)) {
            return false;
            }
                // sprawdzam czy autor już istnieje w bazie danych, jeżeli nie - dodaję go na podstawie wprowadzonej nazwy.
            if(!this.CheckIfAuthorExists(authorName)){
                this.InsertAuthor(authorName, null);
            }
            // pobieram ID autora
            int authorID = this.fetchAuthorsID(authorName);
            // inicjuje InputStream
            InputStream fs = null;
            // jeżeli jest zdjęcie inicjuję FileInputStream
            if(image != null) {
                try {
                    fs = new FileInputStream(image);
                } catch (FileNotFoundException ex) {
                    System.err.println(ex);
                }
            }
            int result = 0;
            // nawiązuje połączenie z bazą danych
            try (Connection connDB = this.conn.getConnection()) {
                // definiuje zapytanie do bazy danych
                String insert = "INSERT INTO songs(songName, songAvatar, authorsID) VALUES (?, ?, ?)";
                // przygotowuję zapytanie
                PreparedStatement query = connDB.prepareStatement(insert);
                // binduje parametry, obraz zapisuję w bazie danych jako BLOB
                query.setString(1, songName);
                query.setBlob(2, fs);
                query.setInt(3,authorID);
                // wykonuje zapytanie
                result = query.executeUpdate();
                if(result == 1){
                    // jeżeli nastąpił sukces oraz nazwa płyty nie jest pusta.
                    // to wprowadzam relacje pomiędzy utworem a płytą
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
        /*
        *Metoda odpowiadająca za sprawdzenie czy dana płyta już istnieje w bazie
        *
        *
         */
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
    /*
     *Metoda odpowiadająca za sprawdzenie czy dany utwór już istnieje w bazie
     *
     *
     */
     private boolean CheckIfSongExists(String songName){
         int result = 0;
         // nawiązuje połączenie z bazą danych
         try (Connection connDB = conn.getConnection()) {
             // przygotowuję zapytanie
             String check = "SELECT count(*) AS total FROM songs WHERE songName= ?";
             PreparedStatement query = connDB.prepareStatement(check);
             query.setString(1, songName);

             ResultSet rs = query.executeQuery();

             while (rs.next()) {
                 // przypisuje do zmiennej result wynik zapytania (zapytania zliczającego).
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
    /*
     *Metoda odpowiadająca za pobranie ID autora
     *
     *
     */
     private int fetchAuthorsID(String authorName){
         int result = 0;
         try (Connection connDB = conn.getConnection()) {
             // przygotowuję zapytanie
             String check = "SELECT IDauthor AS id FROM authors WHERE authorName= ?";
             PreparedStatement query = connDB.prepareStatement(check);
             query.setString(1, authorName);
            // wykonuję zapytanie
             ResultSet rs = query.executeQuery();
             System.out.println("RS"+rs);

             while (rs.next()) {
                 // przypisuje do zmiennej result pobrane ID
                result = rs.getInt("id");
             }
         }catch (SQLException e) {
        System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
    }
         // zwracam wynik
        return result;
     }
    /*
     *Metoda odpowiadająca za pobranie ID danej płyty
     *
     *
     */
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
    /*
     *Metoda odpowiadająca za pobranie ID danego utworu
     *
     *
     */
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
    /*
     *Metoda odpowiadająca za dodanie relacji do tabeli wiążącej utwór z płytą.
     *
     *
     */
    private boolean InsertSongtoDiskRelation(int diskID, int songID){
        int result = 0;
        try (Connection connDB = this.conn.getConnection()) {
            // wywołuję połączenie z bazą danych oraz przygotowuję zapytanie.
            String insert = "INSERT INTO songstodisksrelation(diskID, songID) VALUES (?, ?)";
            PreparedStatement query = connDB.prepareStatement(insert);
            query.setInt(1, diskID);
            query.setInt(2, songID);
            // wykonuję zapytanie
            result = query.executeUpdate();
            if(result == 1){
                // zwracam true, jeżeli sukces
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
