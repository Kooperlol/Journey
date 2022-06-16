package me.kooper.fbla.util;

import me.kooper.fbla.App;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogUtil {

    // logger used throughout the program to log information
    public static final Logger LOGGER = Logger.getLogger("logger");

    // creates logger and log file for the session
    public static void init() {
        FileHandler fileHandler;
        try {
            String time = java.time.LocalTime.now().toString();
            time = time.replaceAll("[.:]", "-");
            File log = App.getStorageManager().makeFile("logs" + System.getProperty("file.separator") + time + ".log");
            if (log == null) {
                LOGGER.severe("Error creating log file! Logger disabled.");
                return;
            }
            fileHandler = new FileHandler(log.getAbsolutePath());
            LOGGER.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            LOGGER.info("Logger Created; session's log will be saved at " + log.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.severe( "Exception: " + e);
        }
    }

}
