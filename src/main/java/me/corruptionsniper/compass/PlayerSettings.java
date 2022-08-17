package me.corruptionsniper.compass;

import java.util.HashMap;
import java.util.UUID;

public class PlayerSettings {

    Settings settings;
    UUID playerUUID;

    HashMap<UUID,Settings> playerSettings = new HashMap<>();

    public HashMap<UUID, Settings> hashmap() {return playerSettings;}

    public Settings getSettings(UUID playerUUID) {return playerSettings.get(playerUUID);}
    public void put(UUID playerUUID,Settings settings) {playerSettings.put(playerUUID,settings);}
    public void remove(UUID playerUUID) {playerSettings.remove(playerUUID);}
}
