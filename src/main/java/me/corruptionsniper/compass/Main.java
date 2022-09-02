package me.corruptionsniper.compass;

import me.corruptionsniper.compass.commands.CompassPointsCommand;
import me.corruptionsniper.compass.commands.SettingsCommand;
import me.corruptionsniper.compass.compassPoints.PluginPlayerCompassPoints;
import me.corruptionsniper.compass.settings.PluginPlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    JsonFiles jsonFiles = new JsonFiles(this);
    PluginPlayerSettings pluginPlayerSettings = new PluginPlayerSettings();
    PluginPlayerCompassPoints pluginPlayerCompassPoints = new PluginPlayerCompassPoints();
    @Override
    public void onEnable() {
        //Loading files to their corresponding classes.
        pluginPlayerSettings.setPlayerSettings(jsonFiles.readPlayerSettingsFile());
        pluginPlayerCompassPoints.setPlayerCompassPoints(jsonFiles.readCompassPointsFile());

        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("compassPoints").setExecutor(new CompassPointsCommand());

        //Registers events to PerPlayerTimer class.
        Bukkit.getPluginManager().registerEvents(new PerPlayerTimer(this),this);

    }

    @Override
    public void onDisable() {
        //Saving classes to their corresponding files.
        jsonFiles.writePlayerSettingsFile(pluginPlayerSettings.getPlayerSettings());
        jsonFiles.writeCompassPointsFile(pluginPlayerCompassPoints.getPlayerCompassPoints());
    }
}
