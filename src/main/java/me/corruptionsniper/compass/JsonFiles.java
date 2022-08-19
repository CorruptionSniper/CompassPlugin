package me.corruptionsniper.compass;

import com.google.gson.Gson;
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

    public void writePlayerSettingsFile(PlayerSettings playerSettings) {
        String fileName = "playerSettings.json";
        File file = instanceFile(fileName);


        try {
            Gson gson = new Gson();
            Writer writer = new FileWriter(file);
            gson.toJson(playerSettings,writer);
            writer.flush();
            writer.close();

            System.out.println("Saved: " + fileName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PlayerSettings readPlayerSettingsFile() {
        String fileName = "playerSettings.json";
        File file = instanceFile(fileName);

        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(file);
            PlayerSettings fileData = gson.fromJson(reader, PlayerSettings.class);
            if (fileData == null) {fileData = new PlayerSettings();}

            System.out.println("Loaded: " + fileName);
            return fileData;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeCompassPointsFile(PlayerCompassPoints playerCompassPoints) {
        String fileName = "compassPoints.json";
        File file = instanceFile(fileName);

        try {
            Gson gson = new Gson();
            Writer writer = new FileWriter(file);
            gson.toJson(playerCompassPoints,writer);
            writer.flush();
            writer.close();

            System.out.println("Saved: " + fileName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PlayerCompassPoints readCompassPointsFile() {
        String fileName = "compassPoints.json";
        File file = instanceFile(fileName);

        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(file);
            PlayerCompassPoints fileData = gson.fromJson(reader, PlayerCompassPoints.class);
            if (fileData == null) {fileData = new PlayerCompassPoints();}

            System.out.println("Loaded: " + fileName);
            return fileData;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
