package me.corruptionsniper.compass;

import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class JsonFiles {

    private final Gson gson = new Gson();
    private final JavaPlugin javaPlugin;
    public JsonFiles(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    // TODO: 19/11/2022 fix json writing/reading
    public File instanceFile(String fileName) {
        javaPlugin.getDataFolder().mkdir();
        File file = new File(javaPlugin.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    public <T> void write(String fileName, T data) {
        File file = instanceFile(fileName);
        try {
            Writer writer = new FileWriter(file);
            gson.toJson(data,writer);
            writer.flush();
            writer.close();

            System.out.println("Saved: " + fileName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T read(String fileName, Class<T> classOfT) {
        File file = instanceFile(fileName);

        try {
            Reader reader = new FileReader(file);
            T data = gson.fromJson(reader,classOfT);

            System.out.println("Loaded: " + fileName);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
