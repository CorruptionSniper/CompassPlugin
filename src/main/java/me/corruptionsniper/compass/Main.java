package me.corruptionsniper.compass;

import me.corruptionsniper.compass.commands.CompassPointsCommand;
import me.corruptionsniper.compass.commands.SettingsCommand;
import me.corruptionsniper.compass.compassPoints.PlayerCompassPoints;
import me.corruptionsniper.compass.settings.PlayerSettings;
import me.corruptionsniper.compass.tabCompleters.CompassPointsTabCompleter;
import me.corruptionsniper.compass.tabCompleters.SettingsTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    JsonFiles jsonFiles = new JsonFiles(this);
    PlayerSettings playerSettings = new PlayerSettings();
    PlayerCompassPoints playerCompassPoints = new PlayerCompassPoints();

    @Override
    public void onEnable() {
        loadFromFiles();
        setExecutors();
        setTabCompleters();
        registerEvents();
    }

    private void loadFromFiles() {
        playerSettings.setMap(jsonFiles.read(playerSettings.getFileName(), playerSettings.getMap().getClass()));
        playerCompassPoints.setMap(jsonFiles.read(playerCompassPoints.getFileName(), playerCompassPoints.getMap().getClass()));
    }

    private void setExecutors() {
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("compassPoints").setExecutor(new CompassPointsCommand());
    }

    private void setTabCompleters() {
        getCommand("settings").setTabCompleter(new SettingsTabCompleter());
        getCommand("compassPoints").setTabCompleter(new CompassPointsTabCompleter());
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PerPlayerTimer(this),this);
    }

    @Override
    public void onDisable() {
        saveToFiles();
    }

    private void saveToFiles() {
        jsonFiles.write(playerSettings.getFileName(), playerSettings.getMap());
        jsonFiles.write(playerCompassPoints.getFileName(), playerCompassPoints.getMap());
    }
}
