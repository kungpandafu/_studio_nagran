package com.example._studio_nagran;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.prefs.Preferences;

public class HelloController {
    @FXML

     Button changeSceneBtn;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField username;
    @FXML
    private PasswordField userPassword;
    @FXML
    private Button loginBtn;
    @FXML
    public void handleLoginOnAction(ActionEvent e){
        if(username.getText().isBlank() == false && userPassword.getText().isBlank() == false){
            authController checkUser = new authController();
           // loginMessageLabel.setText("zalogowano");
            if(checkUser.signInUser(username.getText(), userPassword.getText())){
                loginMessageLabel.setText("zalogowano");
                    //redirect to home scene.
                   try{ redirectAuthenticatedUser();}catch(Exception ex){
                       System.err.println("Error");
                   };

                }
            }

        else{
           loginMessageLabel.setStyle("-fx-text-fill: red;");
           loginMessageLabel.setText("Oba pola muszą być wypełnione!");
        }
    }

    public void handleChangeScenetoRegister() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("register-view.fxml"));

        Stage window = (Stage) changeSceneBtn.getScene().getWindow();
        window.setScene(new Scene(root,640,480));
    }
    public void redirectAuthenticatedUser() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("home-view.fxml"));

        Stage window = (Stage) changeSceneBtn.getScene().getWindow();
        window.setScene(new Scene(root,640,480));
        window.setResizable(true);
    }
}