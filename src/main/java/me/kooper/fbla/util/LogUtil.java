package me.kooper.fbla.util;

import lombok.Getter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogUtil {

    // logger used throughout the program to log information
    @Getter private static final Logger logger = Logger.getLogger("logger");

    // creates logger and log file for the session
    public static void init() {
        FileHandler fileHandler;
        try {
            // looks to see if directory of journey exists for logs; if not creates it and the saved-locations file too
            File journey = new File(System.getProperty("user.home") + "/journey/logs");
            if (!journey.exists()) {
                journey.mkdirs();
                LogUtil.getLogger().log(Level.INFO, "Created local log directory at " + journey.getAbsolutePath());
            }
            String time = java.time.LocalTime.now().toString();
            time = time.replaceAll("[.:]", "-");
            File log = new File(System.getProperty("user.home") + "/journey/logs/log-" + time + ".log");
            log = new File(String.valueOf(Files.createFile(Path.of(log.getAbsolutePath()))));
            fileHandler = new FileHandler(log.getAbsolutePath());
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.info("Logger Created; session's log will be saved at " + log.getAbsolutePath());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Exception: ", e);
        }
    }

}
