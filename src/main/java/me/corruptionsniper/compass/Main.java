package me.corruptionsniper.compass;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
    JsonFiles jsonFiles = new JsonFiles(this);
    PluginPlayerSettings pluginPlayerSettings = new PluginPlayerSettings();
    PluginPlayerCompassPoints pluginPlayerCompassPoints = new PluginPlayerCompassPoints();
    @Override
    public void onEnable() {
        pluginPlayerSettings.setPlayerSettings(jsonFiles.readPlayerSettingsFile());
        pluginPlayerCompassPoints.setPlayerCompassPoints(jsonFiles.readCompassPointsFile());
        //Registers events to PerPlayerTimer class.
        Bukkit.getPluginManager().registerEvents(new PerPlayerTimer(this),this);

    }

    @Override
    public void onDisable() {
        jsonFiles.writePlayerSettingsFile(pluginPlayerSettings.getPlayerSettings());
        jsonFiles.writeCompassPointsFile(pluginPlayerCompassPoints.getPlayerCompassPoints());
    }
}
