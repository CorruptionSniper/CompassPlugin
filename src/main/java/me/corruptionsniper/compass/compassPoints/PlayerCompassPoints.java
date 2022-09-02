package me.corruptionsniper.compass.compassPoints;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerCompassPoints {
    HashMap<UUID, List<CompassPoint>> compassPointsHashMap = new HashMap<>();

    public List<CompassPoint> get(UUID playerUUID) {
        return compassPointsHashMap.get(playerUUID);
    }
    public void put(UUID playerUUID,List<CompassPoint> compassPointList) {
        compassPointsHashMap.put(playerUUID, compassPointList);
    }
    public boolean find(UUID playerUUID) {
        return compassPointsHashMap.containsKey(playerUUID);
    }
}
