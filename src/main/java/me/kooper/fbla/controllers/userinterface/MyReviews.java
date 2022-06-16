package me.kooper.fbla.controllers.userinterface;

import com.mongodb.client.FindIterable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.kooper.fbla.App;
import me.kooper.fbla.managers.MongoManager;
import me.kooper.fbla.models.User;
import me.kooper.fbla.util.LogUtil;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.mongodb.client.model.Filters.eq;

public class MyReviews implements Initializable {

    // retrieve FXML gui components to manipulate
    @FXML ListView<Label> reviewDisplay;
    @FXML Label loading;

    // stores the users reviews from the review collection
    private FindIterable<Document> myReviews;

    // switch to saved locations page
    @FXML
    public void openSavedLocations() {
        App.setRoot("userinterface/SavedLocations");
    }

    // switch to the browse locations page
    @FXML
    public void openBrowseLocations() {
        App.setRoot("userinterface/BrowseLocations");
    }

    /* On a new thread, it updates the reviews by getting all documents within the reviews
    collection that contain the name of the user. Then, it updates the display by adding each
    review. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LogUtil.LOGGER.info( "Updating user's reviews on a new thread:");
        reviewDisplay.getItems().clear();
        ExecutorService threadPool = Executors.newWorkStealingPool();
        threadPool.execute(() -> {
            LogUtil.LOGGER.info( "Getting reviews that match the user's name...");
            myReviews = MongoManager.getReviews().find(eq("user", User.getUserName()));
            Platform.runLater(() -> {
                myReviews.forEach(myReview -> {
                    Label label = new Label("Place " + myReview.getString("place") + " | Rating: " + myReview.getDouble("stars") + " | Review: \n" + myReview.getString("review"));
                    if (myReview.getString("review").equals("")) {
                        label.setText(label.getText().substring(0, label.getText().lastIndexOf("|")));
                    }
                    label.setMaxWidth(reviewDisplay.getWidth());
                    label.setWrapText(true);
                    reviewDisplay.getItems().add(label);
                });
                loading.setVisible(false);
            });
        });
        LogUtil.LOGGER.info( "Found " + reviewDisplay.getItems().size() + " reviews written by the user.");
        LogUtil.LOGGER.info( "Updated review display of user's reviews!");
    }

    // Called when the user clicks the edit review button
    @FXML
    public void editReview() throws IOException {
        /* check if a review is selected in the table and if so gets the review info and
        creates a stage with the edit review fxml and controller */
        LogUtil.LOGGER.info( "Edit review request; checking if user selected a review...");
        ArrayList<Document> reviews = myReviews.into(new ArrayList<>());
        if (!reviewDisplay.getSelectionModel().isEmpty()) {
            LogUtil.LOGGER.info( "Loading Edit Review FXML file...");
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/userinterface/EditReview.fxml"));
            Parent root = loader.load();
            LogUtil.LOGGER.info( "Loaded; now passing review to the controller...");
            EditReview editReview = loader.getController();
            editReview.init(reviews.get(reviewDisplay.getSelectionModel().getSelectedIndex()));
            LogUtil.LOGGER.info( "Done; now creating new stage to set content...");
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Edit Review");
            stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("media/Journey.png"))));
            stage.show();
            LogUtil.LOGGER.info( "Done; edit review stage shown as popup to user!");
        }
    }

}
