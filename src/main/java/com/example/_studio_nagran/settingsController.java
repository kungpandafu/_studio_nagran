package com.example._studio_nagran;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.scene.Scene;

public class settingsController implements Initializable
{
    private static Collection<String> getStyleSheets() {}
    @FXML
    public ChoiceBox<String> lauguage1;
    @FXML
    public ChoiceBox<String> layout_colour1;
    @FXML
    public TextField studio_name1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lauguage1.setItems(FXCollections.observableArrayList("Polski", "English"));
        lauguage1.getSelectionModel().selectFirst();
        lauguage1.valueProperty().addListener(new ChangeListener<String>()
        {
            @Override

                public void changed (ObservableValue < ? extends String > observable, String oldValue, String newValue)
                {
                    if (newValue.equals("Polski")) {

                    } else if (newValue.equals("English")) {

                    }
                }

        });
        ///tu reszta





        layout_colour1.setItems(FXCollections.observableArrayList("Dark","White"));
        layout_colour1.valueProperty().addListener(new ChangeListener<String>()
        {
            @Override

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {

                if (newValue.equals("Dark")) {
                    addProductsController.getStyleSheets().add("dark-theme.css");
                    HelloApplication.getStyleSheets().add("dark-theme.css");
                    settingsController.getStyleSheets().add("dark-theme.css");
                }
                else if (newValue.equals("Bright"))
                {
                    addProductsController.getStyleSheets().add("whitemode.css");
                }


            }
        });



    }




}