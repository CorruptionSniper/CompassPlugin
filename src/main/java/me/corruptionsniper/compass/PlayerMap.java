package me.corruptionsniper.compass;

import java.util.HashMap;
import java.util.UUID;

public abstract class PlayerMap<V> implements SerializeMap<UUID, V> {

    public abstract String getFileName();

    public abstract HashMap<UUID, V> getMap();

    public abstract void setMap(HashMap<UUID, V> dataMap);

    public V get(UUID playerUUID, V playerData) {
        if (playerData == null) {
            playerData = restoreDefaults(playerUUID);
        }
        return playerData;
    }

    public abstract V put(UUID playerUUID, V value);

    public V restoreDefaults(UUID playerUUID) {
        V defaults = defaults();
        put(playerUUID,defaults);
        return defaults;
    }

    public abstract V defaults();
}
