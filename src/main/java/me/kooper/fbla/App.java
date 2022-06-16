package me.kooper.fbla;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import me.kooper.fbla.managers.StorageManager;
import me.kooper.fbla.util.LogUtil;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends Application {

    // stores scene and storage manager
    private static Stage stage;
    private static Scene scene;
    private static StorageManager storageManager;

    // called from main method on application start
    @Override
    public void start(Stage stage) {

        // set stage options
        App.stage = stage;

        // create local storage manager
        storageManager = new StorageManager();

        // set up logging for database and application
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.WARNING);
        LogUtil.init();

        // prompt login screen
        LogUtil.LOGGER.info( "Launching Login Screen...");
        scene = new Scene(loadFXML("Login"));
        stage.setScene(scene);
        setRoot("Login");
        stage.centerOnScreen();

        // set stage settings
        stage.setTitle("Journey");
        stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("media/Journey.png"))));

        // show app to user
        stage.show();
        LogUtil.LOGGER.info( "Login Screen Prompted.");
    }

    // sets the scene from fxml file name
    public static void setRoot(String fxml) {
        LogUtil.LOGGER.info( "Setting scene to: " + fxml);
        scene.setRoot(loadFXML(fxml));
        stage.setMinHeight(scene.getHeight());
        stage.setMinWidth(scene.getWidth());
    }

    // get fxml loader from file name
    private static Parent loadFXML(String fxml) {
        try {
            return FXMLLoader.load(App.class.getResource("fxml/" + fxml + ".fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // calls start method and init javafx
    public static void main(String[] args) {
        launch();
    }

    // get storage manager
    public static StorageManager getStorageManager() {
        return storageManager;
    }

}