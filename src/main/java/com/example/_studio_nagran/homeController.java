package com.example._studio_nagran;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class homeController {
    @FXML
    private Button addProductsBtn;
    @FXML
    private Label userData;
    // get user prefs
    //Preferences userPreferences = Preferences.userRoot();
    //               // String user = userPreferences.get("username", "username" );
// String permissions= userPreferences.get("permissions", "permissions" );
// if(permissions !=2)
    public void navigateToAddProducts(MouseEvent e) throws Exception{

        FXMLLoader fxmlloader = new FXMLLoader();
       fxmlloader.setLocation(getClass().getResource("add-products.fxml"));
        Scene scene = new Scene(fxmlloader.load(), 600, 400);
       Stage stage = new Stage();
       stage.setTitle("Dodawanie Nowości");
       stage.setScene(scene);
        stage.show();
    }
}
