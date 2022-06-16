package me.kooper.fbla.managers;

import lombok.Getter;
import me.kooper.fbla.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public class StorageManager {

    private final File JOURNEYDIR;

    public StorageManager() {
        // looks to see if directory of journey exists for logs; if not creates it and the saved-locations file too
        File journey = new File(System.getProperty("user.home") + "/journey");
        if (!journey.exists()) {
            journey.mkdirs();
            LogUtil.LOGGER.info( "Created local log directory at " + journey.getAbsolutePath());
        }
        JOURNEYDIR = journey;
    }

    public File makeFile(String path) {
        try {
            File file = new File(this.getJOURNEYDIR() + System.getProperty("file.separator") + path);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                return new File(String.valueOf(Files.createFile(Path.of(file.getAbsolutePath()))));
            } else {
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
