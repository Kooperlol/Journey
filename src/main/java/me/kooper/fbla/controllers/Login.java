package me.kooper.fbla.controllers;

import com.mongodb.client.MongoCollection;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import me.kooper.fbla.App;
import me.kooper.fbla.managers.MongoManager;
import me.kooper.fbla.models.User;
import me.kooper.fbla.util.HashUtil;
import me.kooper.fbla.util.LogUtil;
import org.bson.Document;

import java.security.NoSuchAlgorithmException;

import static com.mongodb.client.model.Filters.eq;

public class Login {

    // retrieve FXML gui components to manipulate
    @FXML private TextField username, passwordShown;
    @FXML private PasswordField passwordHide;
    @FXML private Label display;
    @FXML private ImageView toggleIcon;

    // get users collection to compare login info to
    private final MongoCollection<Document> USERS = MongoManager.getUsers();

    // switch to sign up page
    @FXML
    private void signUP() {
        App.setRoot("SignUp");
    }

    /* Activated from login button and attempts to log the user in by comparing credentials
    to database. If successful, the user is prompted to the browse locations tab. */
    @FXML
    private void login() throws NoSuchAlgorithmException {

        // get username provided and find document matching the username
        String user = username.getText().toLowerCase();
        Document login = USERS.find(eq("_id", user)).first();

        if (login != null) {
            // compares password to hashed one stored in database
            HashUtil passwordShownHash = new HashUtil(passwordShown.getText());
            HashUtil passwordHidenHash = new HashUtil(passwordHide.getText());

            // compares data to make sure the login details are correct
            if (login.getString("password").equals(passwordShownHash.getEncryptedValue()) || login.getString("password").equals(passwordHidenHash.getEncryptedValue())) {
                // open the browse locations screen
                LogUtil.LOGGER.info( "Login information correct: login success!");
                LogUtil.LOGGER.info( "Redirecting user to browse locations page...");
                App.setRoot("userinterface/BrowseLocations");

                // stores username in global data, so it can utilize throughout the program
                User.setUserName(user);
                LogUtil.LOGGER.info( "Username stored in User class to be accessed throughout session.");
            } else {
                invalidCredentials();
            }
        } else {
            invalidCredentials();
        }
    }

    // throw invalid credentials
    private void invalidCredentials() {
        LogUtil.LOGGER.info( "Login Failed: Invalid Credentials.");
        username.setText("");
        passwordShown.setText("");
        passwordHide.setText("");
        display.setId("error");
        display.setText("Invalid credentials");
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
        pauseTransition.setOnFinished(e -> {
            display.setText("");
            display.setId("");
        });
        pauseTransition.play();
    }

    // show and hide password
    @FXML
    private void togglePassword() {
        LogUtil.LOGGER.info( "Toggling password visibility.");
        Image icon;
        if (passwordShown.isVisible()) {
            icon = new Image("me/kooper/fbla/media/SHOW_PASSWORD.png");
            passwordShown.setVisible(false);
            passwordHide.setVisible(true);
            passwordHide.setText(passwordShown.getText());
        } else {
            icon = new Image("me/kooper/fbla/media/HIDE_PASSWORD.png");
            passwordHide.setVisible(false);
            passwordShown.setVisible(true);
            passwordShown.setText(passwordHide.getText());
        }
        toggleIcon.setImage(icon);
    }

}