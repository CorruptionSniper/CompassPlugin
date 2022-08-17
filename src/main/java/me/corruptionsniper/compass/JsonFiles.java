package me.corruptionsniper.compass;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
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

    public void setPlayerSettings(Player player, Settings settings) {
        File file = instanceFile("playerSettings.json");

        PlayerSettings data = new PlayerSettings();
        data.put(player.getUniqueId(),settings);

        try {
            Gson gson = new Gson();
            Writer writer = new FileWriter(file,false);
            gson.toJson(data,writer);
            writer.flush();
            writer.close();

            System.out.println("written data?");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Settings getPlayerSettings(Player player) {
        File file = instanceFile("playerSettings.json");

        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(file);
            PlayerSettings fileData = gson.fromJson(reader, PlayerSettings.class);
            Settings settingsData = fileData.getSettings(player.getUniqueId());
            if (settingsData == null) {
                Settings defaultSettings = new Settings(true);
                setPlayerSettings(player,defaultSettings);
                return defaultSettings;
            }
            else {return settingsData;}


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*public CompassPoints getPlayerCompassPoints(Player player) {
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader("compassPointsData.json");
            CompassPoints compassPointsData = gson.fromJson(reader,CompassPoints.class);
            return compassPointsData;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}
