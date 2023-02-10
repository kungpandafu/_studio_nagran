module com.example._studio_nagran {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.prefs;


    opens com.example._studio_nagran to javafx.fxml;
    exports com.example._studio_nagran;
}