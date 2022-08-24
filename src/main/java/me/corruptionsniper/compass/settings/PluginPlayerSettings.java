package me.corruptionsniper.compass.settings;

import org.bukkit.entity.Player;

public class PluginPlayerSettings {
    private static PlayerSettings playerSettings;

    public PlayerSettings getPlayerSettings() {
        return playerSettings;
    }
    public void setPlayerSettings(PlayerSettings playerSettings) {
        PluginPlayerSettings.playerSettings = playerSettings;
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
    public void changeSetting(Player player, String setting, Boolean value) {
        switch (setting) {
            case "compass":
                playerSettings.get(player.getUniqueId()).setCompass(value);
                break;
        }
    }

    public void restoreDefaults(Player player) {
        Settings defaultSettings = new Settings(true);
        playerSettings.put(player.getUniqueId(),defaultSettings);
    }
}
