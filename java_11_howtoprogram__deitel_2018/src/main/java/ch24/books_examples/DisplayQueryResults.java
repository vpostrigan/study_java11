package ch24.books_examples;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Fig. X: DisplayQueryResults.java
// Main application class that loads and displays the GUI.
public class DisplayQueryResults extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("DisplayQueryResults.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Display Query Results");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
