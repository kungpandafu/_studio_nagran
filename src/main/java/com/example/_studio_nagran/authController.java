package com.example._studio_nagran;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import org.mindrot.jbcrypt.*;
import java.util.prefs.Preferences;

public class authController {

    // deklaruje połączenie z bazą danych.
    private final DatabaseController conn = new DatabaseController();

    /*
     * @Param String
     * @Param String
     * Metoda odpowiedzialna za rejestracje użytkownika w bazie danych, jako parametr przyjmuje username oraz password.
     */
    protected boolean registerUser(String username, String password) {
        if (!this.checkIfUserAlreadyExists(username)) {
            return false;
        }

        int result = 0;
        int permissions= 0;
        if(this.checkifFirst()) permissions = 2;

        try (Connection connDB = this.conn.getConnection()) {
            // deklaruje połączenie jako preparedStatement
            String register = "INSERT INTO users (USERNAME, PASSWORD, PERMISSIONS) VALUES (?,?, ?)";
            PreparedStatement query = connDB.prepareStatement(register);
            // binduje wartości
            query.setString(1, username);
            // Używam biblioteki BCrypt do hashowania hasła.
            query.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            query.setInt(3, 2);
            result = query.executeUpdate();
            if (result == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /*
     * @Param username
     *
     * Metoda sprawdza czy podany użytkownik już istnieje w bazie danych zwraca True|False w zależności od wyniku.
     */
    private boolean checkIfUserAlreadyExists(String username) {
        int result = 0;
        try (Connection connDB = conn.getConnection()) {
            String check = "SELECT count(*) AS total FROM users WHERE username = ?";
            PreparedStatement query = connDB.prepareStatement(check);
            query.setString(1, username);

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                result = rs.getInt("total");
            }
            if (result == 0) return true;
            else return false;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
    /*
    *@Param String
    *@Param String
    *
    * Metoda odpowiedzialna za "zalogowanie" użytkownika, zwraca True | False w zależności od wyniku
     */
    protected boolean signInUser(String username, String password) {
        try (Connection connDB = conn.getConnection()) {
            String fetchedUsername = null;
            String fetchedPassword = null;
            Integer fetchedpermissions = null;
            String check = "SELECT username AS fetcheduser, password AS fetcheduserpwd, permissions AS fetchedpermissions FROM users WHERE username = ? LIMIT 1";
            PreparedStatement query = connDB.prepareStatement(check);
            query.setString(1, username);
            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                fetchedUsername = rs.getString("fetcheduser");
                fetchedPassword = rs.getString("fetcheduserpwd");
                fetchedpermissions = rs.getInt("fetchedpermissions");
            }
            if(this.checkforCredentials(username, fetchedUsername, password, fetchedPassword)){
                // Wykorzystuję mechanizm Preferencji do przechowywania danych użytkownika.
                Preferences userPreferences = Preferences.userRoot();
                userPreferences.put("username",fetchedUsername);
                userPreferences.putInt("permissions", fetchedpermissions);
                return true;

            }


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
    * @Param String
    * @Param String
    * @Param String
    * @Param String
    *
    * Metoda odpowiada za porównanie wartości wprowadzonych przez użytkownika przy logowaniu, do wartości otrzymanych w bazie danych
    * Zwraca True | False w zależności od rezultatu
     */

    private boolean checkforCredentials(String username, String fetchedusername, String password, String fetchedpwd) {
        if(fetchedusername.equals(username) && BCrypt.checkpw(password, fetchedpwd)) return true;
        else return false;
    }
    /*
    *Metoda odpowiedzialna za sprawdzenie czy jest to pierwszy użytkownik w bazie danych
    * Zwraca True | False w zależności od wyniku.
    *
     */
    private boolean checkifFirst(){
        int result = 0;
        try (Connection connDB = conn.getConnection()) {
            String check = "SELECT count(*) AS total FROM users";
            PreparedStatement query = connDB.prepareStatement(check);


            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                result = rs.getInt("total");
            }
            if (result == 0) return true;
            else return false;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

