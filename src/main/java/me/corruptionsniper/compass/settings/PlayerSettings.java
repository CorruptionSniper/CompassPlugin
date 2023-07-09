package me.corruptionsniper.compass.settings;

import me.corruptionsniper.compass.PlayerMap;

import java.util.HashMap;
import java.util.UUID;

public class PlayerSettings extends PlayerMap<Settings> {

    private static HashMap<UUID, Settings> playerSettingsMap =  new HashMap<>();

    @Override
    public String getFileName() {
        return "playerSettings.json";
    }

    @Override
    public HashMap<UUID, Settings> getMap() {
        return playerSettingsMap;
    }

    @Override
    public void setMap(HashMap<UUID, Settings> dataMap) {
        if (dataMap != null) playerSettingsMap = dataMap;
    }

    public Settings get(UUID playerUUID) {
        return super.get(playerUUID, privateGet(playerUUID));
    }

    private Settings privateGet(UUID playerUUID) {
        return playerSettingsMap.get(playerUUID);
    }

    @Override
    public Settings put(UUID playerUUID, Settings settings) {
        return playerSettingsMap.put(playerUUID, settings);
    }

    @Override
    public Settings restoreDefaults(UUID playerUUID) {
        Settings defaults = defaults();
        put(playerUUID,defaults);
        return defaults;
    }

    @Override
    public Settings defaults() {
        return new Settings(true,4,70,1,1920,1080);
    }
}
