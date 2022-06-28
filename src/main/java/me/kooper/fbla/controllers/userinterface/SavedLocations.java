package me.kooper.fbla.controllers.userinterface;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import me.kooper.fbla.App;
import me.kooper.fbla.models.Place;
import me.kooper.fbla.util.LogUtil;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SavedLocations implements Initializable {

    // retrieve FXML gui components to manipulate
    @FXML ListView<String> locationsViewer;

    // stores the saved locations as place models
    private final ArrayList<Place> OUTPUT = new ArrayList<>();

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
        LogUtil.LOGGER.info( "Refreshing saved locations...");

        // clear old data
        locationsViewer.getItems().clear();
        OUTPUT.clear();

        // get or create file for saved locations
        File file = App.getStorageManager().makeFile("saved-locations.txt");

        // reads data within saved-locations.txt and converts json object to pojo of PlaceModel. Then, the location is added to the viewable list.
        LogUtil.LOGGER.info( "Reading " + file.getAbsolutePath() + " for user's saved locations:");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        for (String data = reader.readLine(); data != null; data = reader.readLine()) {
            Place model = new Place(data);
            locationsViewer.getItems().add(model.getNAME());
            OUTPUT.add(model);
        }
        LogUtil.LOGGER.info( "Updated saved-locations table!");
    }

    /* Open selected location from view list in a new scene
     Called by clicking the 'View Location' button */
    @FXML
    public void openLocation(ActionEvent event) throws IOException {
        LogUtil.LOGGER.info( "Open location button clicked; checking user selected a location...");

        // check if something is selected
        if (!locationsViewer.getSelectionModel().isEmpty()) {
            // Gets place model from selected index out of hashmap
            Place selected = OUTPUT.get(locationsViewer.getSelectionModel().getSelectedIndex());

            LogUtil.LOGGER.info( "Loading Location Template FXML and passing location into controller...");
            // Load location template controller and pass the place model for content
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/userinterface/LocationTemplate.fxml"));
            Parent root = loader.load();
            LocationTemplate locationTemplate = loader.getController();
            locationTemplate.init(selected);
            LogUtil.LOGGER.info( "Loaded!");

            LogUtil.LOGGER.info( "Getting stage to load location template onto...");
            // Get the stage from the action and set the scene
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.show();
            LogUtil.LOGGER.info( "Done; stage shown to user!");
        }
    }

    // unsave selected location
    public void unsaveLocation() throws IOException {
        LogUtil.LOGGER.info( "Unsave location button clicked; checking user selected a location...");

        // check if something is selected
        if (!locationsViewer.getSelectionModel().isEmpty()) {
            // Gets place model from selected index out of hashmap
            Place selected = OUTPUT.get(locationsViewer.getSelectionModel().getSelectedIndex());

            // get or create file for saved locations and read it
            File file = App.getStorageManager().makeFile("saved-locations.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // create string builder from the file contents ignoring the line to remove
            LogUtil.LOGGER.info( "Writing all file data to a string builder except for the data to remove...");
            StringBuilder stringBuilder = new StringBuilder();
            String currentLine;
            LogUtil.LOGGER.info( System.getProperty("line.separator"));
            while((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(selected.getDATA())) continue;
                stringBuilder.append(currentLine).append(System.getProperty("line.separator"));
            }
            reader.close();
            LogUtil.LOGGER.info( "Done!");

            // delete file contents
            LogUtil.LOGGER.info( "Deleting all file data at " + file.getAbsolutePath());
            PrintWriter pw = new PrintWriter(file);
            pw.close();
            LogUtil.LOGGER.info( "Deleted!");

            // write new contents from string builder
            LogUtil.LOGGER.info( "Writing new contents...");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.append(stringBuilder.toString());
            writer.close();
            LogUtil.LOGGER.info( "Done; file updated and location removed, now sending refresh request for saved locations!");

            // refresh the editor
            refresh();
        }
    }

}
