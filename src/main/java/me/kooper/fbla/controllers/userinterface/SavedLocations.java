package me.kooper.fbla.controllers.userinterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import me.kooper.fbla.App;
import me.kooper.fbla.api.place.PlaceModel;
import me.kooper.fbla.util.LogUtil;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class SavedLocations implements Initializable {

    // retrieve FXML gui components to manipulate
    @FXML ListView<String> locationsViewer;

    // stores the saved locations as place models
    private final ArrayList<PlaceModel> output = new ArrayList<>();

    // switch to browse locations page
    @FXML
    public void openBrowseLocations() throws IOException {
        App.setRoot("userinterface/BrowseLocations");
    }

    // switch to my reviews page
    @FXML
    public void openMyReviews() throws IOException {
        App.setRoot("userinterface/MyReviews");
    }

    // when the fxml file loads it provokes the refresh method
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Reads the saved locations file by line and takes the json object and creates place model with it.
    Then, with the place model it adds it to an array list called output and to the locations view display. */
    public void refresh() throws IOException {
        LogUtil.getLogger().log(Level.INFO, "Refreshing saved locations...");

        // clear old data
        locationsViewer.getItems().clear();
        output.clear();

        LogUtil.getLogger().log(Level.INFO, "Checking if local data directory exists; if not create one...");
        // looks to see if directory of journey exists; if not creates it and the saved-locations file too
        File file = new File(System.getProperty("user.home") + "/journey/saved-locations.txt");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            Files.createFile(Path.of(file.getAbsolutePath()));
            LogUtil.getLogger().log(Level.INFO, "Created local data directory at " + file.getAbsolutePath());
        }

        // reads data within saved-locations.txt and converts json object to pojo of PlaceModel. Then, the location is added to the viewable list.
        LogUtil.getLogger().log(Level.INFO, "Reading " + file.getAbsolutePath() + " for user's saved locations:");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        for (String data = reader.readLine(); data != null; data = reader.readLine()) {
            PlaceModel model = new PlaceModel(data);
            locationsViewer.getItems().add(model.getName());
            output.add(model);
        }
        LogUtil.getLogger().log(Level.INFO, "Updated saved-locations table!");
    }

    /* Open selected location from view list in a new scene
     Called by clicking the 'View Location' button */
    @FXML
    public void openLocation(ActionEvent event) throws IOException {
        LogUtil.getLogger().log(Level.INFO, "Open location button clicked; checking user selected a location...");

        // check if something is selected
        if (!locationsViewer.getSelectionModel().isEmpty()) {
            // Gets place model from selected index out of hashmap
            PlaceModel selected = output.get(locationsViewer.getSelectionModel().getSelectedIndex());

            LogUtil.getLogger().log(Level.INFO, "Loading Location Template FXML and passing location into controller...");
            // Load location template controller and pass the place model for content
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/userinterface/LocationTemplate.fxml"));
            Parent root = loader.load();
            LocationTemplate locationTemplate = loader.getController();
            locationTemplate.init(selected);
            LogUtil.getLogger().log(Level.INFO, "Loaded!");

            LogUtil.getLogger().log(Level.INFO, "Getting stage to load location template onto...");
            // Get the stage from the action and set the scene
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.show();
            LogUtil.getLogger().log(Level.INFO, "Done; stage shown to user!");
        }
    }

    // unsave selected location
    public void unsaveLocation() throws IOException {
        LogUtil.getLogger().log(Level.INFO, "Unsave location button clicked; checking user selected a location...");

        // check if something is selected
        if (!locationsViewer.getSelectionModel().isEmpty()) {
            // Gets place model from selected index out of hashmap
            PlaceModel selected = output.get(locationsViewer.getSelectionModel().getSelectedIndex());

            // get the file from the resources directory
            LogUtil.getLogger().log(Level.INFO, "Getting file from " + System.getProperty("user.home") + "/journey/saved-locations.txt");
            File file = new File(System.getProperty("user.home") + "/journey/saved-locations.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // create string builder from the file contents ignoring the line to remove
            LogUtil.getLogger().log(Level.INFO, "Writing all file data to a string builder except for the data to remove...");
            StringBuilder stringBuilder = new StringBuilder();
            String currentLine;
            LogUtil.getLogger().log(Level.INFO, System.getProperty("line.separator"));
            while((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(selected.getData())) continue;
                stringBuilder.append(currentLine).append(System.getProperty("line.separator"));
            }
            reader.close();
            LogUtil.getLogger().log(Level.INFO, "Done!");

            // delete file contents
            LogUtil.getLogger().log(Level.INFO, "Deleting all file data at " + file.getAbsolutePath());
            PrintWriter pw = new PrintWriter(file);
            pw.close();
            LogUtil.getLogger().log(Level.INFO, "Deleted!");

            // write new contents from string builder
            LogUtil.getLogger().log(Level.INFO, "Writing new contents...");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.append(stringBuilder.toString());
            writer.close();
            LogUtil.getLogger().log(Level.INFO, "Done; file updated and location removed, now sending refresh request for saved locations!");

            // refresh the editor
            refresh();
        }
    }

}
