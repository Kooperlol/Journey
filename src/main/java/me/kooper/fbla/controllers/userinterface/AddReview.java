package me.kooper.fbla.controllers.userinterface;

import com.mongodb.client.MongoCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import me.kooper.fbla.User;
import me.kooper.fbla.util.LogUtil;
import me.kooper.fbla.util.MongoManager;
import me.kooper.fbla.api.place.PlaceModel;
import org.bson.Document;
import org.controlsfx.control.Rating;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class AddReview {

    // retrieve FXML gui components to manipulate
    @FXML TextArea review;
    @FXML Rating rating;

    // get the review collection to add a review to
    private final MongoCollection<Document> reviewCollection = MongoManager.getReviews();

    // stores the location the review is being added to
    private PlaceModel location;

    // the controller of the location to update the reviews once submitted
    private LocationTemplate controller;

    // initiate location model and controller
    public void init(PlaceModel location, LocationTemplate controller) {
        this.location = location;
        this.controller = controller;
    }

    @FXML
    public void submitReview(ActionEvent event) {
        LogUtil.getLogger().log(Level.INFO, "Submit review button clicked.");

        // close review window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        // get star rating
        Double stars = rating.getRating();

        // get review and format spacing
        String placeReview = review.getText();
        placeReview = placeReview.replaceAll("(\\t|\\r?\\n)+", " ");
        placeReview = placeReview.trim().replaceAll(" +", " ");

        // create review document
        Document review = new Document();
        review.append("placeid", location.getPlaceID());
        review.append("place", location.getName());
        review.append("user", User.getUserName());
        review.append("stars", stars);
        review.append("review", placeReview);
        reviewCollection.insertOne(review);

        LogUtil.getLogger().log(Level.INFO, "Successfully added review to reviews collection:");
        LogUtil.getLogger().log(Level.INFO, review.toJson());

        // update review list
        LogUtil.getLogger().log(Level.INFO, "Updating reviews list...");
        ExecutorService threadPool = Executors.newWorkStealingPool();
        threadPool.execute(() -> controller.updateReviews());
    }

}
