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
    private final JsonFiles jsonFiles = new JsonFiles(this);
    private final PlayerSettings playerSettings = new PlayerSettings();
    private final PlayerCompassPoints playerCompassPoints = new PlayerCompassPoints();

    private final String playerSettingsFileName = "playerSettings.json";
    private final String compassPointsFileName = "compassPoints.json";

    @Override
    public void onEnable() {
        //Loading files to their corresponding classes.
        PlayerSettings.setMap(jsonFiles.read(playerSettingsFileName, playerSettings.getMap().getClass()));
        PlayerCompassPoints.setMap(jsonFiles.read(compassPointsFileName, playerCompassPoints.getMap().getClass()));

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
        jsonFiles.write(playerSettingsFileName, playerSettings.getMap());
        jsonFiles.write(compassPointsFileName, playerCompassPoints.getMap());
    }
}
