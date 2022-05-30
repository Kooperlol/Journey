package me.kooper.fbla;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import me.kooper.fbla.util.LogUtil;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends Application {

    // stores scene and stage of the application
    private static Scene scene;

    // called from main method on application start
    @Override
    public void start(Stage stage) throws IOException {

        // set up logging for database and application
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.WARNING);
        LogUtil.init();

        // prompt login screen
        LogUtil.getLogger().log(Level.INFO, "Launching Login Screen...");
        scene = new Scene(loadFXML("Login"));
        stage.setScene(scene);

        // set stage settings
        stage.setTitle("Journey");
        stage.setMinWidth(1176);
        stage.setMinHeight(657);
        stage.centerOnScreen();
        stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("media/Journey.png"))));

        // show app to user
        stage.show();
        LogUtil.getLogger().log(Level.INFO, "Login Screen Prompted.");
    }

    // sets the scene from fxml file name
    public static void setRoot(String fxml) throws IOException {
        LogUtil.getLogger().log(Level.INFO, "Setting scene to: " + fxml);
        scene.setRoot(loadFXML(fxml));
    }

    // get fxml loader from file name
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // calls start method and init javafx
    public static void main(String[] args) {
        launch();
    }

}