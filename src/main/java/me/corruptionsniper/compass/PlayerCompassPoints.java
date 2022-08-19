package me.corruptionsniper.compass;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerCompassPoints {
    HashMap<UUID, List<CompassPoints>> compassPointsHashMap = new HashMap<>();

    public List<CompassPoints> get(UUID playerUUID) {return compassPointsHashMap.get(playerUUID);}
    public void put(UUID playerUUID,List<CompassPoints> compassPointsList) {compassPointsHashMap.put(playerUUID,compassPointsList);}
    public void remove(UUID playerUUID) {compassPointsHashMap.remove(playerUUID);}
    public boolean find(UUID playerUUID) {return compassPointsHashMap.containsValue(playerUUID);}
}
