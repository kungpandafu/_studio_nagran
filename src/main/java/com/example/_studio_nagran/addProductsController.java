package com.example._studio_nagran;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
public class addProductsController implements Initializable {
    // inicjuje połączenie z bazą danych
    private final DatabaseController conn = new DatabaseController();

    // definiuje potrzebnę elementy GUI
    @FXML
    private GridPane formGrid;
    @FXML
    private ComboBox<String> comboBox;

    private Button handleActionBtn;
    private Button sendAvatarBtn;
    private TextField authorname;
    private TextField diskName;
    private TextField songName;
    @FXML
    private ImageView userAvatar;
    @FXML
    private Label successerrlabel;
    private File selectedFile = null;


    // nadpisuje metodę initalize
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // tworzę pole wyboru - którego wartość będzie definiowało - które elementy UI się pojawią wypełniam je zawartością oraz
        //  inicjuje potrzebne elementy.
        comboBox.setItems(FXCollections.observableArrayList("Wybierz Opcję", "Autor", "Utwór", "Płyta"));
        comboBox.getSelectionModel().selectFirst();
        sendAvatarBtn = new Button();
        sendAvatarBtn.setText("Prześlij Grafikę");
        sendAvatarBtn.getStyleClass().add("sendAvatarBtn");
        handleActionBtn = new Button();
        handleActionBtn.getStyleClass().add("handleActionBtn");
        authorname = new TextField();
        authorname.getStyleClass().add("formfield");
        handleActionBtn.setText("Dodaj rekord do Bazy Danych");
        TextField diskName = new TextField();
        TextField songName = new TextField();
        diskName.getStyleClass().add("formfield");
        songName.getStyleClass().add("formfield");

        // dodaje EventListener do pola wyboru
        comboBox.valueProperty().addListener(new ChangeListener<String>() {

            // nadpisuję metodę Changed, która będzie wywoływana przy każdej zmianie w polu wyboru "ComboBox"
            @Override

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // sprawdzam czy wybrana wartość to Autor, następnie wyświetlam odpowiednie elementy UI
                if (newValue.equals("Autor")) {
                    formGrid.getChildren().retainAll(formGrid.getChildren().get(0));
                    authorname.setPromptText("Wprowadź nazwę Autora");


                    formGrid.add(authorname, 0, 0);


                    formGrid.add(sendAvatarBtn, 0, 1);
                    formGrid.add(handleActionBtn, 0, 2);

                    System.out.println("Selected value : " + newValue);
                    // sprawdzam czy wybrana wartość to Płyta, następnie wyświetlam odpowiednie elementy UI
                } else if (newValue.equals("Płyta")) {
                    formGrid.getChildren().retainAll(formGrid.getChildren().get(0));
                    diskName.setPromptText("Wprowadź Nazwę Płyty");
                    // authorname = new TextField();
                    // authorname.setPromptText("Wprowadź Nazwę Autora");
                    System.out.println("Selected value : " + newValue);
                    formGrid.add(diskName, 0, 0);
                    // formGrid.add(authorname, 0,1);
                    formGrid.add(sendAvatarBtn, 0, 1);
                    formGrid.add(handleActionBtn, 0, 2);
                    //System.out.println("Selected value : " + newValue);
                    // sprawdzam czy wybrana wartość to Utwór, następnie wyświetlam odpowiednie elementy UI
                } else if (newValue.equals("Utwór")) {
                    formGrid.getChildren().retainAll(formGrid.getChildren().get(0));
                    songName.setPromptText("Wprowadź Nazwę Utworu");
                    diskName.setPromptText("Podaj nazwę płyty - przypisz do płyty");
                    authorname.setPromptText("Podaj autora - przypisz autora do utworu");
                   // System.out.println("Selected value : " + newValue);
                    formGrid.add(songName, 0, 0);
                    formGrid.add(diskName, 0, 1);
                    formGrid.add(authorname, 0, 2);
                    formGrid.add(sendAvatarBtn, 0, 3);
                    formGrid.add(handleActionBtn, 0, 4);

                }
                else{
                    // Jeżeli wartość pola wyboru jest inna niż trzy powyższe tj. wynosi ona np. "Wybierz Opcję" - usuwam wszystkie elementy formularza.
                    formGrid.getChildren().retainAll(formGrid.getChildren().get(0));
                }
            }

        });

            //definiuje FileChooser, dzięki któremu będę mógł przesłać obraz do dalszego przetworzenia.
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg")
                , new FileChooser.ExtensionFilter("PNG Files", "*.png")
        );

        sendAvatarBtn.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(sendAvatarBtn.getScene().getWindow());
            if (selectedFile != null) {
                String imagepath = selectedFile.getPath();
                System.out.println("file:" + imagepath);
                System.out.println("file2:" + selectedFile);
                Image image = new Image(imagepath);
                // wyświetlam przesłany obrazek na podglądzie.
                userAvatar.setImage(image);

            }

        });

            // przypisuję Event Handler do przycisku
        handleActionBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // wywołuję kontroler odpowiedzialny za operacje zw. z przesłaniem danych do Bazy
                handleNewProductsController performAction = new handleNewProductsController();
                // sprawdzam wartość pola Wyboru, następnie na jej podstawię definiuje, którą metodę wywołać w handleNewProductsController.
                if (comboBox.getValue() == "Autor") {
                    if (performAction.InsertAuthor(authorname.getText(), selectedFile)) {
                        // jeżeli sukces - wyświetlam stosowną informację
                        successerrlabel.setText("Operacja zakończona sukcesem!");
                        successerrlabel.setTextFill(Color.GREEN);
                    } else {
                        // jeżeli błąd - wyświetlam informacje, że wystąpił błąd.
                        successerrlabel.setText("Wystąpił Błąd! Sprawdź Czy Autor już nie występuje w bazie danych!");
                        successerrlabel.setTextFill(Color.RED);
                    }

                   // System.out.println(authorname.getText());
                   // System.out.println(selectedFile);
                } else if (comboBox.getValue() == "Płyta") {
                    if (diskName.getText().isBlank() || selectedFile == null) {
                        successerrlabel.setText("Wystąpił Błąd! Nazwa Płyty nie może być pusta, grafika musi zostać wybrana!");
                        successerrlabel.setTextFill(Color.RED);
                    }
                    if (performAction.InsertDisk(diskName.getText(), selectedFile)) {
                        successerrlabel.setText("Operacja zakończona sukcesem!");
                        successerrlabel.setTextFill(Color.GREEN);
                    } else {
                        successerrlabel.setText("Wystąpił Błąd! Sprawdź Czy Płyta już nie występuje w bazie danych!");
                        successerrlabel.setTextFill(Color.RED);
                    }
                } else if (comboBox.getValue() == "Utwór") {
                    if (songName.getText().isBlank() || selectedFile == null || authorname.getText().isBlank()) {
                        successerrlabel.setText("Wystąpił Błąd! Nazwa Utworu  nie może być pusta, grafika musi zostać wybrana!");
                        successerrlabel.setTextFill(Color.RED);
                    }

                    if (performAction.InsertSongs(songName.getText(), selectedFile, authorname.getText(), diskName.getText())) {
                        successerrlabel.setText("Operacja zakończona sukcesem!");
                        successerrlabel.setTextFill(Color.GREEN);
                    } else {
                        successerrlabel.setText("Wystąpił Błąd! Sprawdź Czy Utwór już nie występuje w bazie danych!");
                        successerrlabel.setTextFill(Color.RED);
                    }
                }


            }

        });

    }



}
