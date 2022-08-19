package me.corruptionsniper.compass;

import java.util.HashMap;
import java.util.UUID;

public class PlayerSettings {
    HashMap<UUID,Settings> settingsHashmap = new HashMap<>();

    public Settings get(UUID playerUUID) {return settingsHashmap.get(playerUUID);}
    public void put(UUID playerUUID,Settings settings) {
        settingsHashmap.put(playerUUID,settings);}
    public void remove(UUID playerUUID) {
        settingsHashmap.remove(playerUUID);}
}
