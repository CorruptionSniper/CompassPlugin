package me.corruptionsniper.compass;

import me.corruptionsniper.compass.commands.CompassPointsCommand;
import me.corruptionsniper.compass.commands.SettingsCommand;
import me.corruptionsniper.compass.compassPoints.PlayerCompassPoints;
import me.corruptionsniper.compass.compassPoints.PluginPlayerCompassPoints;
import me.corruptionsniper.compass.settings.PlayerSettings;
import me.corruptionsniper.compass.settings.PluginPlayerSettings;
import me.corruptionsniper.compass.tabCompleters.CompassPointsTabCompleter;
import me.corruptionsniper.compass.tabCompleters.SettingsTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    JsonFiles jsonFiles = new JsonFiles(this);
    PluginPlayerSettings pluginPlayerSettings = new PluginPlayerSettings();
    PluginPlayerCompassPoints pluginPlayerCompassPoints = new PluginPlayerCompassPoints();

    String playerSettingsFileName = "playerSettings.json";
    String compassPointsFileName = "compassPoints.json";

    @Override
    public void onEnable() {
        //Loading files to their corresponding classes.
        pluginPlayerSettings.setPlayerSettings(jsonFiles.read(playerSettingsFileName, PlayerSettings.class));
        pluginPlayerCompassPoints.setPlayerCompassPoints((jsonFiles.read(compassPointsFileName, PlayerCompassPoints.class)));

        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("compassPoints").setExecutor(new CompassPointsCommand());

        getCommand("settings").setTabCompleter(new SettingsTabCompleter());
        getCommand("compassPoints").setTabCompleter(new CompassPointsTabCompleter());

        //Registers events to PerPlayerTimer class.
        Bukkit.getPluginManager().registerEvents(new PerPlayerTimer(this),this);

    }

    @Override
    public void onDisable() {
        //Saving classes to their corresponding files.
        jsonFiles.write(playerSettingsFileName,pluginPlayerSettings.getPlayerSettings());
        jsonFiles.write(compassPointsFileName,pluginPlayerCompassPoints.getPlayerCompassPoints());
    }
}
