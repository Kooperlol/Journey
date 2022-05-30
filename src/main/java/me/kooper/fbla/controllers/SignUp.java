package me.kooper.fbla.controllers;

import com.mongodb.client.MongoCollection;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import me.kooper.fbla.App;
import me.kooper.fbla.util.Hasher;
import me.kooper.fbla.User;
import me.kooper.fbla.util.LogUtil;
import me.kooper.fbla.util.MongoManager;
import org.bson.Document;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

import static com.mongodb.client.model.Filters.eq;

public class SignUp {

    // retrieve FXML gui components to manipulate
    @FXML private TextField username;
    @FXML private PasswordField password, confirmPassword;
    @FXML private Label display;

    // switch to login page
    @FXML
    private void login() throws IOException {
        App.setRoot("Login");
    }

    /* Activated from register button and attempts to create and save the credentials in the database.
     If successful, the user is prompted to the user panel on the browse locations tab */
    @FXML
    private void createAccount() throws IOException, NoSuchAlgorithmException {
        LogUtil.getLogger().log(Level.INFO, "Create Account button clicked:");

        String user = username.getText().toLowerCase();

        // makes sure all credentials are filled in
        if (user.length() != 0 && password.getText().length() != 0 && confirmPassword.getText().length() != 0) {
            if (password.getText().equals(confirmPassword.getText())) {

                // checks to see if the username already exists
                final MongoCollection<Document> users = MongoManager.getUsers();
                Document login = users.find(eq("username", user)).first();

                // if account name does not exist create one
                if (login == null) {
                    LogUtil.getLogger().log(Level.INFO, "Creating account...");

                    // create document and store username
                    login = new Document();
                    login.append("username", user);

                    // hashes password to securely store it in case of data breach
                    Hasher hasher = new Hasher(password.getText());
                    login.append("password", hasher.getEncryptedValue());

                    // inserts the new user in the user collection within the database
                    users.insertOne(login);

                    LogUtil.getLogger().log(Level.INFO, "New user inserted to database:");
                    LogUtil.getLogger().log(Level.INFO, login.toJson());
                    LogUtil.getLogger().log(Level.INFO, "Redirecting user to browse locations page...");

                    // open the browse locations scene
                    App.setRoot("userinterface/BrowseLocations");

                    // store the username in user so it can be used throughout the program
                    User.setUserName(user);
                    LogUtil.getLogger().log(Level.INFO, "Username stored in User class to be accessed throughout session.");
                } else {
                    invalidCredentials("Username Taken");
                }
            } else {
                invalidCredentials("Passwords Do Not Match");
            }
        } else {
            invalidCredentials("Please Fill All Fields Below");
        }
    }

    private void invalidCredentials(String reason) {
        LogUtil.getLogger().log(Level.INFO, "Sign Up Failed: " + reason);
        display.setText(reason);
        display.setId("error");
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
        pauseTransition.setOnFinished(e -> {
            display.setText("");
            display.setId("");
        });
        pauseTransition.play();
    }

}