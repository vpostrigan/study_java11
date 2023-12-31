package ch22.FontCSS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// FontCSS.java
public class FontCSS extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root =
                FXMLLoader.load(getClass().getResource("FontCSS.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Draw Stars");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

