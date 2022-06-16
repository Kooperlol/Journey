package me.kooper.fbla.controllers.userinterface;

import com.mongodb.client.MongoCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import me.kooper.fbla.managers.MongoManager;
import me.kooper.fbla.models.Place;
import me.kooper.fbla.models.User;
import me.kooper.fbla.util.LogUtil;
import org.bson.Document;
import org.controlsfx.control.Rating;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddReview {

    // retrieve FXML gui components to manipulate
    @FXML private TextArea review;
    @FXML private Rating rating;

    // get the review collection to add a review to
    private final MongoCollection<Document> reviewCollection = MongoManager.getReviews();

    // stores the location the review is being added to
    private Place location;

    // the controller of the location to update the reviews once submitted
    private LocationTemplate controller;

    // initiate location model and controller
    public void init(Place location, LocationTemplate controller) {
        this.location = location;
        this.controller = controller;
    }

    @FXML
    public void submitReview(ActionEvent event) {
        LogUtil.LOGGER.info( "Submit review button clicked.");

        // close review window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        // get star rating
        final double stars = rating.getRating();

        // get review and format spacing
        String placeReview = review.getText();
        placeReview = placeReview.replaceAll("(\\t|\\r?\\n)+", " ");
        placeReview = placeReview.trim().replaceAll(" +", " ");

        ExecutorService threadPool = Executors.newWorkStealingPool();
        final String finalPlaceReview = placeReview;
        threadPool.execute(() -> {
            // create review document
            Document review = new Document();
            review.append("placeid", location.getPLACEID());
            review.append("place", location.getNAME());
            review.append("user", User.getUserName());
            review.append("stars", stars);
            review.append("review", finalPlaceReview);
            reviewCollection.insertOne(review);

            LogUtil.LOGGER.info( "Successfully added review to reviews collection:");
            LogUtil.LOGGER.info(review.toJson());

            // update review list
            LogUtil.LOGGER.info( "Updating reviews list...");
            controller.updateReviews();
        });
    }

}
