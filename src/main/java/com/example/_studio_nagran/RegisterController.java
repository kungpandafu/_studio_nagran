package com.example._studio_nagran;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class RegisterController{
    @FXML
    private TextField username;
    @FXML
    private PasswordField userPassword;
    @FXML
    private PasswordField userPasswordRepeat;
    @FXML
    Button backToLoginBtn;
    @FXML
    private Button registerBtn;
    @FXML
    private Label registerMessageLabel;
    @FXML

        /*
        * Metoda odpowiedzialna za przeprowadzenie procesu rejestracji użytkownika
        * @Param ActionEvent
        *
         */
    public void registerBtnOnAction(ActionEvent e){
        if(username.getText().isBlank() == false && userPassword.getText().isBlank() == false && userPasswordRepeat.getText().isBlank() == false)
        {
            if(userPassword.getText().equals(userPasswordRepeat.getText())){
                // wywołuję authController
                authController performUserRegister = new authController();
                // wywołuję metodę odpowiedzialną za zarejestrowanie użytkownika w authControllerze
                boolean result = performUserRegister.registerUser(username.getText(), userPassword.getText());
                if(result){
                  try{
                      // jeżeli sukces przekierowuję z powrotem na widok logowania.
                      this.handleBackToLogin();
                  }
                  catch(Exception ex){
                      ex.printStackTrace();
                  }
                }
                else{
                    registerMessageLabel.setText("Błąd!");
                    registerMessageLabel.setStyle("-fx-text-fill: red;");
                }
            }
                else{
                registerMessageLabel.setText("Hasła muszą się zgadzać!");
                registerMessageLabel.setStyle("-fx-text-fill: red;");
                     }
    }
        else{
        registerMessageLabel.setText("Wszystkie pola muszą być uzupełnione!");
        registerMessageLabel.setStyle("-fx-text-fill: red;");
        }
    }
    /*
    * Metoda odpowiedzialna za przekierowanie użytkownika na widok logowania po sukcesywnej rejestracji
    *
    *
     */
    public void handleBackToLogin() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

        Stage window = (Stage) backToLoginBtn.getScene().getWindow();
        window.setScene(new Scene(root,640,480));
    }

}
