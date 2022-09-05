package me.corruptionsniper.compass.settings;

import org.bukkit.entity.Player;

public class PluginPlayerSettings {
    private static PlayerSettings playerSettings;

    public PlayerSettings getPlayerSettings() {
        return playerSettings;
    }
    public void setPlayerSettings(PlayerSettings playerSettings) {
        if (playerSettings != null) {PluginPlayerSettings.playerSettings = playerSettings;}
        else {PluginPlayerSettings.playerSettings = new PlayerSettings();}
    }

    public Settings get(Player player) {
        if (playerSettings.get(player.getUniqueId()) == null) {
            restoreDefaults(player);
        }
        return playerSettings.get(player.getUniqueId());
    }
    public void put(Player player, Settings settings) {
        playerSettings.put(player.getUniqueId(),settings);
    }


    public void restoreDefaults(Player player) {
        Settings defaultSettings = new Settings(true,3,70,1,1920,1080);
        playerSettings.put(player.getUniqueId(),defaultSettings);
    }


}
