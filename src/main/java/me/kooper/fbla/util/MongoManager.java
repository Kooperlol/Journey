package me.kooper.fbla.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;

import java.util.logging.Level;

public class MongoManager {

    // get client
    public static MongoClient getClient() {
        LogUtil.getLogger().log(Level.INFO, "Getting MongoDB Client.");

        // load project environmental variables
        Dotenv dotenv = Dotenv.load();

        // connect and create mongo client from DB_CONNECTION_KEY in environmental file and return it
        return MongoClients.create(dotenv.get("DB_CONNECTION_KEY"));
    }

    // get database "journey"
    public static MongoDatabase getDatabase() {
        LogUtil.getLogger().log(Level.INFO, "Getting Journey Database.");
        return getClient().getDatabase("journey");
    }

    // collection getters
    public static MongoCollection<Document> getUsers() {
        LogUtil.getLogger().log(Level.INFO, "Getting Users Collection.");
        return getDatabase().getCollection("users");
    }

    public static MongoCollection<Document> getReviews() {
        LogUtil.getLogger().log(Level.INFO, "Getting Reviews Collection.");
        return getDatabase().getCollection("reviews");
    }

}
