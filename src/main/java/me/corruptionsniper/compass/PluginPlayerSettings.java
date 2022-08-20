package me.corruptionsniper.compass;

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
}
