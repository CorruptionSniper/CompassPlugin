package me.corruptionsniper.compass;

import com.google.gson.Gson;
import me.corruptionsniper.compass.compassPoints.PlayerCompassPoints;
import me.corruptionsniper.compass.settings.PlayerSettings;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class JsonFiles {

    private JavaPlugin javaPlugin;
    public JsonFiles(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public File instanceFile(String name) {
        javaPlugin.getDataFolder().mkdir();
        File file = new File(javaPlugin.getDataFolder(), name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    String playerSettingsFile = "playerSettings.json";

    public void writePlayerSettingsFile(PlayerSettings playerSettings) {
        File file = instanceFile(playerSettingsFile);


        try {
            Gson gson = new Gson();
            Writer writer = new FileWriter(file);
            gson.toJson(playerSettings,writer);
            writer.flush();
            writer.close();

            System.out.println("Saved: " + playerSettingsFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PlayerSettings readPlayerSettingsFile() {
        File file = instanceFile(playerSettingsFile);

        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(file);
            PlayerSettings fileData = gson.fromJson(reader, PlayerSettings.class);
            if (fileData == null) {fileData = new PlayerSettings();}

            System.out.println("Loaded: " + playerSettingsFile);
            return fileData;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String compassPointsFileName = "compassPoints.json";
    public void writeCompassPointsFile(PlayerCompassPoints playerCompassPoints) {

        File file = instanceFile(compassPointsFileName);

        try {
            Gson gson = new Gson();
            Writer writer = new FileWriter(file);
            gson.toJson(playerCompassPoints,writer);
            writer.flush();
            writer.close();

            System.out.println("Saved: " + compassPointsFileName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PlayerCompassPoints readCompassPointsFile() {
        File file = instanceFile(compassPointsFileName);

        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(file);
            PlayerCompassPoints fileData = gson.fromJson(reader, PlayerCompassPoints.class);
            if (fileData == null) {fileData = new PlayerCompassPoints();}

            System.out.println("Loaded: " + compassPointsFileName);
            return fileData;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
