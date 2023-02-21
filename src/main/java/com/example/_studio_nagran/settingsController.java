package com.example._studio_nagran;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import  javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class settingsController
{
    @FXML
    public ChoiceBox<String> lauguage1;
    @FXML
    public ChoiceBox<String> layout_colour1;
    @FXML
    public TextField studio_name1;

    //language1.setItems(FXCollections.observableArrayList("Polski", "English"));
    //language1.getSelectionModel().selectFirst();
}