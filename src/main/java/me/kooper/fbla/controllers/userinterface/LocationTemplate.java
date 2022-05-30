package me.kooper.fbla.controllers.userinterface;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.kooper.fbla.App;
import me.kooper.fbla.api.place.PlaceModel;
import me.kooper.fbla.util.LogUtil;
import me.kooper.fbla.util.MongoManager;
import org.bson.Document;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class LocationTemplate {

    // retrieve FXML gui components to manipulate
    @FXML Label feedback, loading, address, name;
    @FXML VBox reviewDisplay;

    // the location information that is currently being viewed stored as a pojo
    private PlaceModel location;

    // get the review collection for showing the reviews of the location
    private final MongoCollection<Document> reviewCollection = MongoManager.getReviews();

    // creating thread pool so reviews can be loaded on another thread
    private final ExecutorService threadPool = Executors.newWorkStealingPool();

    // provoked when user clicks hyperlink to go back to browsing locations
    @FXML
    public void openBrowseLocations() throws IOException {
        App.setRoot("userinterface/BrowseLocations");
    }

    // initiate location model
    public void init(PlaceModel location) {
        this.location = location;
        address.setText(location.getFormatted());
        name.setText(location.getName());

        // load reviews in new thread
        threadPool.execute(this::updateReviews);
    }

    /* provoked when user clicks the add review button on the sidebar and loads the add
    review fxml file and controller on a new window with the location model passed into it */
    @FXML
    public void addReview() throws IOException {
        LogUtil.getLogger().log(Level.INFO, "Add review request:");
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/userinterface/AddReview.fxml"));
        Parent root = loader.load();
        LogUtil.getLogger().log(Level.INFO, "Loaded AddReview FXML file.");
        AddReview addReview = loader.getController();
        addReview.init(location, this);
        LogUtil.getLogger().log(Level.INFO, "Passed location into controller and now loading it onto a new stage:");
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle(location.getName() + " - Add Review");
        stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("media/Journey.png"))));
        stage.show();
        LogUtil.getLogger().log(Level.INFO, "Done; add review window has been show to the user!");
    }

    /* updates the reviews from the review collection with all the documents containing the placeid
    called when fxml loads and is run on a different thread */
    @FXML
    public void updateReviews() {

        LogUtil.getLogger().log(Level.INFO, "Updating reviews for location...");

        Platform.runLater(() -> reviewDisplay.getChildren().clear());

        // get documents containing the placeid within the reviews collection
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("placeid", location.getPlaceID());

        // loop through each result and make a display
        LogUtil.getLogger().log(Level.INFO, "Searching through review collection for all reviews matching placeid:");
        for (Document review : reviewCollection.find(whereQuery)) {
            // create vbox that stores the information for the looped review
            VBox reviewItem = new VBox();
            reviewItem.setSpacing(5);

            // create label to display user who wrote review
            Label user = new Label("User: " + review.getString("user"));
            user.setTextFill(Color.WHITE);
            user.setId("text");

            // creates label to display the rating for review
            Label rating = new Label("Rating: " + review.getDouble("stars"));
            rating.setTextFill(Color.WHITE);
            rating.setId("text");

            // create label to display the review written
            Label reviewText = new Label("Review: \n" + review.getString("review"));
            reviewText.setTextFill(Color.WHITE);
            reviewText.setId("text");
            reviewText.setWrapText(true);

            // adds the user, review, and rating label to the vbox display
            reviewItem.getChildren().addAll(user, rating, reviewText);
            reviewItem.setPadding(new Insets(5, 5, 5, 5));
            reviewItem.setId("gradient");

            // add review to the list of review display which will contain all the reviews
            Platform.runLater(() -> reviewDisplay.getChildren().add(reviewItem));
        }
        Platform.runLater(() -> {
            loading.setVisible(false);
            LogUtil.getLogger().log(Level.INFO, "Done; reviews updated for location!");
            LogUtil.getLogger().log(Level.INFO, "Total reviews for location: " + reviewDisplay.getChildren().size());
        });
    }

    // called when the user clicks the save location button
    @FXML
    public void saveLocation() throws IOException {

        LogUtil.getLogger().log(Level.INFO, "Save location request:");

        // flag to discriminate if the user has saved the location already or not
        boolean valid = true;

        // looks to see if directory of journey exists; if not creates it and the saved-locations file too
        LogUtil.getLogger().log(Level.INFO, "Checking to see if local data exists:");
        File file = new File(System.getProperty("user.home") + "/journey/saved-locations.txt");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            Files.createFile(Path.of(file.getAbsolutePath()));
            LogUtil.getLogger().log(Level.INFO, "No local storage are found so made one at " + Path.of(file.getAbsolutePath()));
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));

        LogUtil.getLogger().log(Level.INFO, "Checking to see if location is already saved:");
        // read each line of the file and set valid to false if the line contains the location already
        for (String data = reader.readLine(); data != null; data = reader.readLine()) {
            if (Objects.equals(data, location.getData().toString())) {
                LogUtil.getLogger().log(Level.INFO, "Location already saved so throwing error!");
                valid = false;
                break;
            }
        }

        // writes the location to the file if it's not in the file otherwise throws error at user
        if (valid) {
            LogUtil.getLogger().log(Level.INFO, "Writing location to local storage file...");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(location.getData().toString()+"\n");
            writer.close();
            LogUtil.getLogger().log(Level.INFO, "Location successfully written to " + file.getAbsolutePath());
            feedback.setId("success");
            feedback.setText("Location successfully saved");
        } else {
            feedback.setId("error");
            feedback.setText("Location already saved");
            LogUtil.getLogger().log(Level.INFO, "Location already saved to local storage!");
        }
        // makes a transition effect to show the response for a period of 3 seconds and then hide it
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
        pauseTransition.setOnFinished(e -> {
            feedback.setText("");
            feedback.setId("");
        });
        pauseTransition.play();
    }
}