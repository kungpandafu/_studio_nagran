package com.example._studio_nagran;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


public class homeController implements Initializable {
    @FXML
    private Button deleteSelectedBtn;
    @FXML
    private Button addProductsBtn;
    @FXML
    private Button logOutBtn;
    @FXML
    private Button shopBtn;
    @FXML
    private Label userData;
    @FXML
    private ComboBox ProductsFilter;
    @FXML
    private TableView itemsTable;
    @FXML
    private TextField editIDtxt;
    @FXML
    private TextField editNametxt;
    @FXML
    private TextField editSongNametxt;
    @FXML
    private TextField editDiskNametxt;
    @FXML
    private Label successlabel;

    // get user prefs
    //Preferences userPreferences = Preferences.userRoot();
    //               // String user = userPreferences.get("username", "username" );
// String permissions= userPreferences.get("permissions", "permissions" );
// if(permissions !=2)
    ObservableList<Author> authors;
    ObservableList<Disk> disks;
    ObservableList<Song> songs;


    TableColumn Identifier = new TableColumn("Identifier");
    TableColumn authorImage = new TableColumn("Obrazek");
    TableColumn diskImage = new TableColumn("Obrazek");
    TableColumn songImage = new TableColumn("Obrazek");
    TableColumn AuthorName = new TableColumn("Nazwa Autora");
    TableColumn songName = new TableColumn("Nazwa Utworu");
    TableColumn relatedTo = new TableColumn("Powiązany/a z");
    TableColumn diskName = new TableColumn<>("Nazwa Płyty");
    int index = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Preferences userPreferences = Preferences.userRoot();
         String user = userPreferences.get("username", "username" );
         String permissions= userPreferences.get("permissions", "permissions" );
         String permText = null;
         if(permissions.equals("0")) permText = "Użytkownik";
         if(permissions.equals("1")) permText = "Administrator";
         if(permissions.equals("2")) permText = "SuperAdministrator";
         userData.setText(user+"\n"+permText);
        ProductsFilter.setItems(FXCollections.observableArrayList("Wybierz Opcję", "Autorzy", "Utwory", "Płyty"));
        ProductsFilter.getSelectionModel().selectFirst();


        ProductsFilter.valueProperty().addListener(new ChangeListener<String>() {
                                                       @Override

                                                       public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {


                                                           if (newValue.equals("Autorzy") || ProductsFilter.getValue().equals("Autorzy")) {
                                                               itemsTable.getItems().clear();
                                                               itemsTable.getColumns().clear();
                                                               authorImage.setCellFactory(p -> {
                                                                   final ImageView imageview = new ImageView();
                                                                   imageview.setFitHeight(100);
                                                                   imageview.setFitWidth(100);

                                                                   TableCell<Author, Image> cell = new TableCell<Author, Image>() {
                                                                       public void updateItem(Image item, boolean empty) {
                                                                           if (item != null) {
                                                                               imageview.setImage(item);
                                                                           }
                                                                       }
                                                                   };
                                                                   cell.setGraphic(imageview);
                                                                   return cell;
                                                               });
                                                               Identifier.setCellValueFactory(new PropertyValueFactory<Author, Integer>("id"));
                                                               AuthorName.setCellValueFactory(new PropertyValueFactory<Author, String>("name"));
                                                               authorImage.setCellValueFactory(new PropertyValueFactory<Author, Image>("authorsAvatar"));
                                                               itemsTable.getColumns().addAll(Identifier, authorImage, AuthorName);
                                                               authors = retrieveProductsController.retrieveAuthors();
                                                               itemsTable.setItems(authors);
                                                           } else if (newValue.equals("Utwory")) {
                                                               itemsTable.getItems().clear();
                                                               itemsTable.getColumns().clear();
                                                               songImage.setCellFactory(p -> {
                                                                   final ImageView imageview = new ImageView();
                                                                   imageview.setFitHeight(100);
                                                                   imageview.setFitWidth(100);

                                                                   TableCell<Song, Image> cell = new TableCell<Song, Image>() {
                                                                       public void updateItem(Image item, boolean empty) {
                                                                           if (item != null) {
                                                                               imageview.setImage(item);
                                                                           }
                                                                       }
                                                                   };
                                                                   cell.setGraphic(imageview);
                                                                   return cell;
                                                               });
                                                               Identifier.setCellValueFactory(new PropertyValueFactory<Song, Integer>("id"));
                                                               songImage.setCellValueFactory(new PropertyValueFactory<Song, Image>("songAvatar"));
                                                               AuthorName.setCellValueFactory(new PropertyValueFactory<Song, String>("authorName"));
                                                               songName.setCellValueFactory(new PropertyValueFactory<Song, String>("name"));
                                                               relatedTo.setCellValueFactory(new PropertyValueFactory<Song, String>("relatedTo"));
                                                               itemsTable.getColumns().addAll(Identifier, songImage, AuthorName, songName, relatedTo);
                                                               songs = retrieveProductsController.retrieveSongs();
                                                               itemsTable.setItems(songs);
                                                               // itemsTable.getColumns().addAll(Identifier, image, AuthorName);

                                                           } else if (newValue.equals("Płyty")) {
                                                               itemsTable.getItems().clear();
                                                               itemsTable.getColumns().clear();
                                                               diskImage.setCellFactory(p -> {
                                                                   final ImageView imageview = new ImageView();
                                                                   imageview.setFitHeight(100);
                                                                   imageview.setFitWidth(100);

                                                                   TableCell<Disk, Image> cell = new TableCell<Disk, Image>() {
                                                                       public void updateItem(Image item, boolean empty) {
                                                                           if (item != null) {
                                                                               imageview.setImage(item);
                                                                           }
                                                                       }
                                                                   };
                                                                   cell.setGraphic(imageview);
                                                                   return cell;
                                                               });
                                                               Identifier.setCellValueFactory(new PropertyValueFactory<Disk, Integer>("id"));
                                                               diskName.setCellValueFactory(new PropertyValueFactory<Disk, String>("name"));
                                                               diskImage.setCellValueFactory(new PropertyValueFactory<Disk, Image>("diskAvatar"));
                                                               itemsTable.getColumns().addAll(Identifier, diskImage, diskName);
                                                               disks = retrieveProductsController.retrieveDisks();
                                                               itemsTable.setItems(disks);
                                                           } else {
                                                               itemsTable.getColumns().clear();
                                                               itemsTable.getItems().clear();

                                                           }
                                                       }
                                                   }
        );
    }

    public void navigateToAddProducts(MouseEvent e) throws Exception {

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("add-products.fxml"));
        Scene scene = new Scene(fxmlloader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Dodawanie Nowości");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void navigateToSettings(MouseEvent e) throws Exception {
        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("settings-view.fxml"));
        Scene scene = new Scene(fxmlloader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Ustawienia Aplikacji");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void logOut(MouseEvent e) throws Exception {
        Platform.exit();
    }

    public void handleShopBtn(ActionEvent e) {
        shopBtn.setDisable(true);


    }

    public void getSelected(MouseEvent e) {
        index = itemsTable.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        editIDtxt.setText(Identifier.getCellData(index).toString());
        editIDtxt.setDisable(true);
        if (ProductsFilter.getValue().equals("Płyty")) {
            editNametxt.setDisable(true);
            editDiskNametxt.setDisable(false);
            editDiskNametxt.setText(diskName.getCellData(index).toString());
            editSongNametxt.setDisable(true);
            editSongNametxt.setText(null);
            editNametxt.setText(null);
        } else if (ProductsFilter.getValue().equals("Autorzy")) {
            editNametxt.setDisable(false);
            editNametxt.setText(AuthorName.getCellData(index).toString());
            editDiskNametxt.setText(null);
            editDiskNametxt.setDisable(true);
            editSongNametxt.setText(null);
            editSongNametxt.setDisable(true);
        } else if (ProductsFilter.getValue() == "Utwory") {
            editNametxt.setDisable(true);
            editNametxt.setText(null);
            editSongNametxt.setText(songName.getCellData(index).toString());
            editDiskNametxt.setDisable(true);
            editDiskNametxt.setText(null);
            editSongNametxt.setDisable(false);

        }
    }

    public void deleteEntry(MouseEvent e) {
        manageProductsController manageProduct = new manageProductsController();
        int selectedID = Integer.parseInt(editIDtxt.getText());
        if (ProductsFilter.getValue() == "Autorzy") {

            if (manageProduct.deleteAuthor(selectedID)) {
                successlabel.setTextFill(Color.LIGHTGREEN);
                successlabel.setText("Usuwanie Autora zakończone sukcesem!  - Odśwież tabele poprzez zmiane filtru");
            }
            ;

        } else if (ProductsFilter.getValue() == "Utwory") {

            if (manageProduct.deleteSong(selectedID)) {
                successlabel.setTextFill(Color.LIGHTGREEN);
                successlabel.setText("Usuwanie Utworu zakończone sukcesem! - Odśwież tabele poprzez zmiane filtru");
            }
            ;

        } else if (ProductsFilter.getValue() == "Płyty") {
            if (manageProduct.deleteDisk(selectedID)) {

                successlabel.setTextFill(Color.LIGHTGREEN);
                successlabel.setText("Usuwanie Płyty zakończone sukcesem!  - Odśwież tabele poprzez zmiane filtru");

            }
            ;

        }

        itemsTable.refresh();
    }

    public void editEntry(MouseEvent e) {
        manageProductsController manageProduct = new manageProductsController();
        int selectedID = Integer.parseInt(editIDtxt.getText());
        if (ProductsFilter.getValue() == "Autorzy") {

            if (manageProduct.updateAuthor(selectedID, editNametxt.getText())) {
                successlabel.setTextFill(Color.LIGHTGREEN);
                successlabel.setText("Edycja Autora zakończona sukcesem!  - Odśwież tabele poprzez zmiane filtru");
            }
            ;

        } else if (ProductsFilter.getValue() == "Utwory") {

            if (manageProduct.updateSong(selectedID, editSongNametxt.getText())) {
                successlabel.setTextFill(Color.LIGHTGREEN);
                successlabel.setText("Edycja Utworu zakończona sukcesem! - Odśwież tabele poprzez zmiane filtru");
            }
            ;

        } else if (ProductsFilter.getValue() == "Płyty") {
            if (manageProduct.updateDisk(selectedID, editDiskNametxt.getText())) {

                successlabel.setTextFill(Color.LIGHTGREEN);
                successlabel.setText("Edycja Płyty zakończona sukcesem!  - Odśwież tabele poprzez zmiane filtru");

            }
            ;
        }
    }
}

