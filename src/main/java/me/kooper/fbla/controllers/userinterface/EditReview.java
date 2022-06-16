package me.kooper.fbla.controllers.userinterface;

import com.mongodb.client.MongoCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import me.kooper.fbla.App;
import me.kooper.fbla.managers.MongoManager;
import me.kooper.fbla.util.LogUtil;
import org.bson.Document;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditReview {

    // retrieve FXML gui components to manipulate
    @FXML TextArea review;
    @FXML Rating rating;

    // retrieve review collection to replace old review info with new review info
    private final MongoCollection<Document> REVIEWCOLLECTION = MongoManager.getReviews();

    // current review information from review wanting to be manipulated
    private Document reviewInformation;

    // initiate location model
    public void init(Document reviewInformation) {
        // initialize review information to edit
        this.reviewInformation = reviewInformation;

        // load current review settings
        review.setText(reviewInformation.getString("review"));
        rating.setRating(reviewInformation.getDouble("stars"));
    }

    // provoked when user clicks button to update the review with new information
    @FXML
    public void updateReview(ActionEvent event) throws IOException {
        LogUtil.LOGGER.info( "Update review request.");

        ExecutorService threadPool = Executors.newWorkStealingPool();
        threadPool.execute(() -> {
            // create new document from old one and update values wanting to be changed
            Document newReview = new Document(reviewInformation);
            newReview.replace("stars", rating.getRating());
            newReview.replace("review", review.getText());

            // replace old review with new review document within the review collection
            REVIEWCOLLECTION.replaceOne(reviewInformation, newReview);
            LogUtil.LOGGER.info( "Replaced old review with new review in reviews collection.");
        });

        // close the edit review window
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        LogUtil.LOGGER.info( "Closed edit review window.");

        // updates the my reviews page
        App.setRoot("userinterface/MyReviews");
    }

    // provoked when the user clicks the delete review button
    @FXML
    public void deleteReview(ActionEvent event) throws IOException {
        LogUtil.LOGGER.info( "Delete review request:");

        // delete the review from the review collection
        ExecutorService threadPool = Executors.newWorkStealingPool();
        threadPool.execute(() -> {
            REVIEWCOLLECTION.deleteOne(reviewInformation);
            LogUtil.LOGGER.info( "Deleted review from reviews collection.");
        });

        // close the edit review window
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        LogUtil.LOGGER.info( "Closed edit review window.");

        // updates the my reviews page
        App.setRoot("userinterface/MyReviews");
    }

}