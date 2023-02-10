package com.example._studio_nagran;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import org.mindrot.jbcrypt.*;
import java.util.prefs.Preferences;

public class authController {
    private final DatabaseController conn = new DatabaseController();

    /*
     * @String username
     * @String password
     * User registration functionality, takes username and password as args.
     */
    protected boolean registerUser(String username, String password) {
        if (!this.checkIfUserAlreadyExists(username)) {
            return false;
        }

        int result = 0;
        try (Connection connDB = this.conn.getConnection()) {

            String register = "INSERT INTO users (USERNAME, PASSWORD, PERMISSIONS) VALUES (?,?, ?)";
            PreparedStatement query = connDB.prepareStatement(register);
            query.setString(1, username);
            query.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            query.setInt(3, 0);
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
     * @String username
     *
     * Function provides functionality that checks if a user with provided username already exists
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
                // set user data
                Preferences userPreferences = Preferences.userRoot();
                userPreferences.put("username",fetchedUsername);
                userPreferences.putInt("permissions", fetchedpermissions);
                return true;
             //   ManageUserSessionController.getInstance(fetchedUsername, fetchedpermissions);
                // redirect to...
            }


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean checkforCredentials(String username, String fetchedusername, String password, String fetchedpwd) {
        if(fetchedusername.equals(username) && BCrypt.checkpw(password, fetchedpwd)) return true;
        else return false;
    }
}

