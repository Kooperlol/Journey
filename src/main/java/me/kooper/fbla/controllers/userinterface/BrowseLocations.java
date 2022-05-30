package me.kooper.fbla.controllers.userinterface;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.experimental.Tolerate;
import me.kooper.fbla.App;
import me.kooper.fbla.api.location.LocationConnection;
import me.kooper.fbla.api.location.LocationModel;
import me.kooper.fbla.api.place.Attributes;
import me.kooper.fbla.api.place.PlaceConnection;
import me.kooper.fbla.api.place.PlaceModel;
import me.kooper.fbla.util.LogUtil;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class BrowseLocations implements Initializable {

    // retrieve FXML gui components to manipulate
    @FXML TextField locationSearch, limit;
    @FXML Button locationExecutable, viewLocation;
    @FXML Label searching;
    @FXML ListView<String> locations;
    @FXML ComboBox<Integer> radius;
    @FXML CheckComboBox<String> categories, conditions;

    // store the output of place models after calling the API
    private ArrayList<PlaceModel> output = new ArrayList<>();

    // get the categories and conditions list from the attributes class
    private final HashMap<String, String> categoryList = new Attributes().getCategories();
    private final HashMap<String, String> conditionsList = new Attributes().getConditions();

    // switch to saved locations page
    @FXML
    public void openSavedLocations() throws IOException {
        App.setRoot("userinterface/SavedLocations");
    }

    // switch to my reviews page
    @FXML
    public void openMyReviews() throws IOException {
        App.setRoot("userinterface/MyReviews");
    }

    // configure things when fxml loads
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // configure categories combo box
        categories.getItems().addAll(categoryList.keySet());

        // configure conditions combo box
        conditions.getItems().addAll(conditionsList.keySet());

        // configure radius combo box
        radius.getItems().addAll(3, 5, 10, 15, 20, 25, 30);

        // add tooltips for help
        Tooltip cityTip = new Tooltip("Enter the city you would like to search for places around.");
        locationSearch.setTooltip(cityTip);

        Tooltip categoriesTip = new Tooltip("Select categories to filter to your desired results.");
        categories.setTooltip(categoriesTip);

        Tooltip conditionsTip = new Tooltip("Selected conditions to filter to your desired results.");
        conditions.setTooltip(conditionsTip);

        Tooltip radiusTip = new Tooltip("Select the radius in miles that will be searched around the city for places.");
        radius.setTooltip(radiusTip);

        Tooltip limitTip = new Tooltip("Limit the number of returned places. If left blank it will default to 500.");
        limit.setTooltip(limitTip);

        Tooltip goTip = new Tooltip("Search for places with the specified details.");
        locationExecutable.setTooltip(goTip);

        Tooltip viewTip = new Tooltip("View the selected location in the table of results.");
        viewLocation.setTooltip(viewTip);
    }

    /* Get all places with desired parameters in a desired radius from Places API
     Called by clicking the 'Go' button */
    @FXML
    public void searchLocations() {
        LogUtil.getLogger().log(Level.INFO, "Searching...");

        // clear old search outputs
        locations.getItems().clear();
        output.clear();

        // show loading text
        searching.setVisible(true);

        // run API requests and searches on new thread
        ExecutorService threadPool = Executors.newWorkStealingPool();
        threadPool.execute(() -> {
            // get the cities lon and lat for the Places API
            if (locationSearch.getText().equals("")) {
                LogUtil.getLogger().log(Level.INFO, "City not filled in so ignoring search request");
                Platform.runLater(() -> searching.setVisible(false));
                return;
            }
            LocationConnection city = new LocationConnection(locationSearch.getText());
            LocationModel cityInfo = new LocationModel(city.getData());

            // Check if the location is actually within Wisconsin
            if (!(cityInfo.getState().equals("Wisconsin"))) {
                Platform.runLater(() -> locations.getItems().add("The specified city is not within Wisconsin."));
                LogUtil.getLogger().log(Level.INFO, "Location user wants to search is not within Wisconsin.");
                Platform.runLater(() -> searching.setVisible(false));
                return;
            }

            // Get selected categories from categoryList from key/display value
            ObservableList<String> categoriesInput = categories.getCheckModel().getCheckedItems();
            StringBuilder categoriesOutput;
            if (!categoriesInput.isEmpty()) {
                categoriesOutput = new StringBuilder(categoryList.get(categoriesInput.get(0)));
                for (int i = 0; i < categoriesInput.size()-1; i++) {
                    categoriesOutput.append(",").append(categoryList.get(categoriesInput.get(i + 1)));
                }
            } else {
                categoriesOutput = new StringBuilder("building");
            }

            // Get selected conditions from conditionList from key/display value
            ObservableList<String> conditionsInput = conditions.getCheckModel().getCheckedItems();
            StringBuilder conditionsOutput;
            if (!conditionsInput.isEmpty()) {
                conditionsOutput = new StringBuilder(conditionsList.get(conditionsInput.get(0)));
                for (int i = 0; i < conditionsInput.size()-1; i++) {
                    conditionsOutput.append(",").append(conditionsList.get(conditionsInput.get(i + 1)));
                }
            } else {
                conditionsOutput = new StringBuilder("named");
            }

            /* Create Place Connection that initializes a connection with the provided specifications
             If some are not specified it will set them to default values */
            PlaceConnection placeConnection = new PlaceConnection(
                categoriesOutput.toString(),
                conditionsOutput.toString(),
                cityInfo.getLon(),
                cityInfo.getLat(),
                radius.getSelectionModel().isEmpty() ? 48280.2 : radius.getValue() * 1609.34,
                !Objects.equals(limit.getText(), "") && Integer.parseInt(limit.getText()) > 0 ? limit.getText() : "500"
            );

            // Adds all places to list view to display to the user
            for (PlaceModel place : placeConnection.getPlaces()) {
                Platform.runLater(() -> locations.getItems().add(place.getName()));
            }

            // set output to the places returned from API request
            output = placeConnection.getPlaces();

            // If no locations are found from the search it will provoke a message
            if (locations.getItems().isEmpty()) {
                Platform.runLater(() -> locations.getItems().add("No results found! Try adjusting your specifications."));
                LogUtil.getLogger().log(Level.INFO, "No results found within search.");
            }

            LogUtil.getLogger().log(Level.INFO, "Successfully ran search; output has been fully updated!");
            Platform.runLater(() -> searching.setVisible(false));
        });
    }

    /* Open selected location from view list in a new scene
     Called by clicking the 'View Location' button */
    @FXML
    public void openLocation(ActionEvent event) throws IOException {
        LogUtil.getLogger().log(Level.INFO, "Open location button clicked; checking if user selected a location...");
        // check if something is selected
        if (!locations.getSelectionModel().isEmpty()) {
            LogUtil.getLogger().log(Level.INFO, "Opening location's page...");

            // Gets place model from selected index out of hashmap
            PlaceModel selected = output.get(locations.getSelectionModel().getSelectedIndex());

            // Load location template controller and pass the place model for content
            LogUtil.getLogger().log(Level.INFO, "Getting Location Template FXML and passing location model into the controller:");
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/userinterface/LocationTemplate.fxml"));
            Parent root = loader.load();
            LocationTemplate locationTemplate = loader.getController();
            locationTemplate.init(selected);
            LogUtil.getLogger().log(Level.INFO, "Done!");

            // Get the stage from the action and set the scene
            LogUtil.getLogger().log(Level.INFO, "Getting stage to load content on:");
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.show();
            LogUtil.getLogger().log(Level.INFO, this.getClass().getName() + ": Done and shown to user!");
        }
    }

}