package ch24.addressbook_example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Fig. X: AddressBook.java
// Main application class that loads and displays the AddressBook's GUI.
public class AddressBook extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AddressBook.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Address Book");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
