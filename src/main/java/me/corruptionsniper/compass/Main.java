package me.corruptionsniper.compass;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin implements Listener {

    JsonFiles jsonFiles = new JsonFiles(this);

    @Override
    public void onEnable() {
        List<PlayerSettings> playersSettings = new ArrayList<>();

        //Registers events to PerPlayerTimer class.
        Bukkit.getPluginManager().registerEvents(new PerPlayerTimer(this),this);

    }
}
