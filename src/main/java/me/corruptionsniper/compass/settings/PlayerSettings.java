package me.corruptionsniper.compass.settings;

import java.util.HashMap;
import java.util.UUID;

public class PlayerSettings {
    private HashMap<UUID, Settings> playerSettingsHashmap = new HashMap<>();

    public Settings get(UUID playerUUID) {
        return playerSettingsHashmap.get(playerUUID);
    }
    public void put(UUID playerUUID,Settings settings) {
        playerSettingsHashmap.put(playerUUID,settings);
    }
    public void remove(UUID playerUUID) {
        playerSettingsHashmap.remove(playerUUID);
    }
}
