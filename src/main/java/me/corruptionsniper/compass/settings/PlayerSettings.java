package me.corruptionsniper.compass.settings;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerSettings {
    private static HashMap<UUID, Settings> playerSettings = new HashMap<>();

    public HashMap<UUID, Settings> getMap() {
        return playerSettings;
    }

    public static void setMap(HashMap<UUID, Settings> playerSettings) {
        PlayerSettings.playerSettings = playerSettings != null ? playerSettings : new HashMap<>();
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
