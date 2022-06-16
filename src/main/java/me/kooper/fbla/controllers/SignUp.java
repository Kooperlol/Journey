package me.kooper.fbla.controllers;

import com.mongodb.client.MongoCollection;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import me.kooper.fbla.App;
import me.kooper.fbla.managers.MongoManager;
import me.kooper.fbla.models.User;
import me.kooper.fbla.util.HashUtil;
import me.kooper.fbla.util.LogUtil;
import org.bson.Document;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.mongodb.client.model.Filters.eq;

public class SignUp {

    // retrieve FXML gui components to manipulate
    @FXML private TextField username;
    @FXML private PasswordField password, confirmPassword;
    @FXML private Label display;

    // switch to login page
    @FXML
    private void login() {
        App.setRoot("Login");
    }

    /* Activated from register button and attempts to create and save the credentials in the database.
     If successful, the user is prompted to the user panel on the browse locations tab */
    @FXML
    private void createAccount() {
        LogUtil.LOGGER.info( "Create Account button clicked:");

        String user = username.getText().toLowerCase();

        // makes sure all credentials are filled in
        if (user.length() != 0 && password.getText().length() != 0 && confirmPassword.getText().length() != 0) {
            if (password.getText().equals(confirmPassword.getText())) {

                // checks to see if the username already exists
                final MongoCollection<Document> users = MongoManager.getUsers();
                Document login = users.find(eq("_id", user)).first();

                // if account name does not exist create one
                if (login == null) {
                    LogUtil.LOGGER.info( "Creating account...");

                    ExecutorService threadPool = Executors.newWorkStealingPool();
                    threadPool.execute(() -> {
                        // create document and store username
                        Document newUser = new Document();
                        newUser.put("_id", user);

                        // hashes password to securely store it in case of data breach
                        HashUtil hasher;
                        try {
                            hasher = new HashUtil(password.getText());
                            newUser.put("password", hasher.getEncryptedValue());
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }

                        // inserts the new user in the user collection within the database
                        users.insertOne(newUser);

                        // log new user
                        LogUtil.LOGGER.info( "New user inserted to database:");
                        LogUtil.LOGGER.info( newUser.toJson());

                        // store the username in user, so it can be used throughout the program
                        User.setUserName(user);
                        LogUtil.LOGGER.info( "Username stored in User class to be accessed throughout session.");
                    });

                    // log redirection to use page
                    LogUtil.LOGGER.info( "Redirecting user to browse locations page...");

                    // open the browse locations scene
                    App.setRoot("userinterface/BrowseLocations");
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
        LogUtil.LOGGER.info( "Sign Up Failed: " + reason);
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