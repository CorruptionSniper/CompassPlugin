package me.corruptionsniper.compass;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;

public class JsonFiles {

    private final JavaPlugin javaPlugin;

    public JsonFiles(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public File getFileInstance(String fileName) {
        if (javaPlugin.getDataFolder().mkdir()) System.out.println("Created directory: " + javaPlugin.getName());
        File file = new File(javaPlugin.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) System.out.println("Created File: " + fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    public <T> void write(String fileName, T data) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = getFileInstance(fileName);

        try {
            Writer writer = new FileWriter(file,false);
            gson.toJson(data, writer);
            writer.close();

            System.out.println(gson.toJson(data));
            System.out.println("Saved: " + fileName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T read(String fileName, Class<T> classOfT) {
        Gson gson = new Gson();

        File file = getFileInstance(fileName);

        try {
            Reader reader = new FileReader(file);
            T data = gson.fromJson(reader,classOfT);
            reader.close();

            System.out.println(gson.toJson(data));
            System.out.println("Loaded: " + fileName);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <K,V,T extends SerializeMap<K,V>>void writeMap(T mapObject) {
        write(mapObject.getFileName(), mapObject.getMap());
    }

    public <K,V,T extends SerializeMap<K,V>> T readMap(T mapObject) {
        HashMap<K,V> readMap = read(mapObject.getFileName(), mapObject.getMap().getClass());
        mapObject.setMap(readMap);
        return mapObject;
    }
}
