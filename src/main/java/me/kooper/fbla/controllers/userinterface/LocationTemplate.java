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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.kooper.fbla.App;
import me.kooper.fbla.managers.MongoManager;
import me.kooper.fbla.models.Place;
import me.kooper.fbla.models.User;
import me.kooper.fbla.util.LogUtil;
import org.bson.Document;
import org.controlsfx.control.Rating;

import java.io.*;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocationTemplate {

    // retrieve FXML gui components to manipulate
    @FXML private Label feedback, loading, address, name, totalReviews;
    @FXML private VBox reviewDisplay;
    @FXML private Rating overallRating;

    // total stars given
    private double totalStars;

    // the location information that is currently being viewed stored as a pojo
    private Place location;

    // get the review collection for showing the reviews of the location
    private final MongoCollection<Document> REVIEWCOLLECTION = MongoManager.getReviews();

    // creating thread pool so reviews can be loaded on another thread
    private final ExecutorService THREADPOOL = Executors.newWorkStealingPool();

    // provoked when user clicks hyperlink to go back to browsing locations
    @FXML
    public void openBrowseLocations() throws IOException {
        App.setRoot("userinterface/BrowseLocations");
    }

    // initiate location model
    public void init(Place location) {
        this.location = location;
        address.setText(location.getFORMATTED());
        name.setText(location.getNAME());

        // load reviews in new thread
        updateReviews();
    }

    /* provoked when user clicks the add review button on the sidebar and loads the add
    review fxml file and controller on a new window with the location model passed into it */
    @FXML
    public void addReview() throws IOException {
        LogUtil.LOGGER.info( "Add review request:");
        LogUtil.LOGGER.info("Checking to make sure user does not have review on this location...");
        Document document = new Document();
        document.put("placeid", location.getPLACEID());
        document.put("user", User.getUserName());
        Document review = REVIEWCOLLECTION.find(document).first();
        if (review != null) {
            feedback.setId("error");
            feedback.setText("You already left a review!");
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
            pauseTransition.setOnFinished(e -> {
                feedback.setText("");
                feedback.setId("");
            });
            pauseTransition.play();
            LogUtil.LOGGER.info("User already has review, stopping request!");
            return;
        }
        LogUtil.LOGGER.info("User does not have review! Prompting window...");
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/userinterface/AddReview.fxml"));
        Parent root = loader.load();
        LogUtil.LOGGER.info( "Loaded AddReview FXML file.");
        AddReview addReview = loader.getController();
        addReview.init(location, this);
        LogUtil.LOGGER.info( "Passed location into controller and now loading it onto a new stage:");
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle(location.getNAME() + " - Add Review");
        stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("media/Journey.png"))));
        stage.show();
        LogUtil.LOGGER.info( "Done; add review window has been show to the user!");
    }

    /* updates the reviews from the review collection with all the documents containing the placeid
    called when fxml loads and is run on a different thread */
    @FXML
    public void updateReviews() {
        THREADPOOL.execute(() -> {
            LogUtil.LOGGER.info( "Updating reviews for location...");

            // clear table and log update
            Platform.runLater(() -> reviewDisplay.getChildren().clear());

            // set to zero
            totalStars = 0;

            // get documents containing the placeid within the reviews collection
            BasicDBObject whereQuery = new BasicDBObject();
            whereQuery.put("placeid", location.getPLACEID());

            // loop through each result and make a display
            LogUtil.LOGGER.info( "Searching through review collection for all reviews matching placeid:");
            for (Document review : REVIEWCOLLECTION.find(whereQuery)) {
                // create vbox that stores the information for the looped review
                VBox reviewItem = new VBox();
                reviewItem.setSpacing(5);

                // create label to display user who wrote review
                Label user = new Label("User: " + review.getString("user"));
                user.setTextFill(Color.WHITE);
                user.setId("text");

                // creates label to display the rating for review
                Label rating = new Label("Rating: " + review.getDouble("stars"));
                totalStars += review.getDouble("stars");
                rating.setTextFill(Color.WHITE);
                rating.setId("text");

                // create label to display the review written
                Label reviewText = null;
                if (!review.getString("review").equals("")) {
                    reviewText = new Label("Review: \n" + review.getString("review"));
                    reviewText.setTextFill(Color.WHITE);
                    reviewText.setId("text");
                    reviewText.setWrapText(true);
                }

                // adds the user, review, and rating label to the vbox display
                if (reviewText != null) {
                    reviewItem.getChildren().addAll(user, rating, reviewText);
                } else {
                    reviewItem.getChildren().addAll(user, rating);
                }
                reviewItem.setPadding(new Insets(5, 5, 5, 5));
                reviewItem.setId("gradient");

                // add review to the list of review display which will contain all the reviews
                Platform.runLater(() -> reviewDisplay.getChildren().add(reviewItem));
            }
            double finalTotalStars = totalStars;
            Platform.runLater(() -> {
                loading.setVisible(false);
                overallRating.setRating(finalTotalStars / reviewDisplay.getChildren().size());
                totalReviews.setText("Total Reviews: " + reviewDisplay.getChildren().size());
                LogUtil.LOGGER.info( "Done; reviews updated for location!");
                LogUtil.LOGGER.info( "Total reviews for location: " + reviewDisplay.getChildren().size());

            });
        });
    }

    // called when the user clicks the save location button
    @FXML
    public void saveLocation() throws IOException {

        LogUtil.LOGGER.info( "Save location request:");

        // flag to discriminate if the user has saved the location already or not
        boolean valid = true;

        // get saved-locations file from local storage
        File file = App.getStorageManager().makeFile("saved-locations.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        LogUtil.LOGGER.info( "Checking to see if location is already saved:");
        // read each line of the file and set valid to false if the line contains the location already
        for (String data = reader.readLine(); data != null; data = reader.readLine()) {
            if (Objects.equals(data, location.getDATA().toString())) {
                LogUtil.LOGGER.info( "Location already saved so throwing error!");
                valid = false;
                break;
            }
        }

        // writes the location to the file if it's not in the file otherwise throws error at user
        if (valid) {
            LogUtil.LOGGER.info( "Writing location to local storage file...");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(location.getDATA().toString()+"\n");
            writer.close();
            LogUtil.LOGGER.info( "Location successfully written to " + file.getAbsolutePath());
            feedback.setId("success");
            feedback.setText("Location successfully saved");
        } else {
            feedback.setId("error");
            feedback.setText("Location already saved");
            LogUtil.LOGGER.info( "Location already saved to local storage!");
        }
        // makes a transition effect to show the response for a period of 3 seconds and then hide it
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
        pauseTransition.setOnFinished(e -> {
            feedback.setText("");
            feedback.setId("");
        });
        pauseTransition.play();
    }

    @FXML
    private void updateRating() {
        overallRating.setRating(totalStars / reviewDisplay.getChildren().size());
    }

}