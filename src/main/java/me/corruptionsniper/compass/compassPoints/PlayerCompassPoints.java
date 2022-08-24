package me.corruptionsniper.compass.compassPoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerCompassPoints {
    HashMap<UUID, List<CompassPoint>> compassPointsHashMap = new HashMap<>();

    public List<CompassPoint> get(UUID playerUUID) {
        if (!compassPointsHashMap.containsKey(playerUUID)) {
            restoreDefaults(playerUUID);
        }
        return compassPointsHashMap.get(playerUUID);
    }
    public void put(UUID playerUUID,List<CompassPoint> compassPointList) {
        compassPointsHashMap.put(playerUUID, compassPointList);
    }
    public void putCompassPoint(UUID playerUUID, CompassPoint compassPoint) {
        List<CompassPoint> compassPointsList = get(playerUUID);
        compassPointsList.add(compassPoint);
        compassPointsHashMap.put(playerUUID, compassPointsList);
    }
    public void remove(UUID playerUUID, CompassPoint compassPoint) {
        List<CompassPoint> compassPointList = compassPointsHashMap.get(playerUUID);
        compassPointList.remove(compassPoint);
        compassPointsHashMap.put(playerUUID,compassPointList);
    }
    public boolean find(UUID playerUUID) {
        return compassPointsHashMap.containsKey(playerUUID);
    }

    public void restoreDefaults(UUID playerUUID) {
        List<CompassPoint> defaultCompassPoints = new ArrayList<>();
        defaultCompassPoints.add(new CompassPoint("North", 0F));
        defaultCompassPoints.add(new CompassPoint("North East",45F));
        defaultCompassPoints.add(new CompassPoint("East",90F));
        defaultCompassPoints.add(new CompassPoint("South East",135F));
        defaultCompassPoints.add(new CompassPoint("South",180F));
        defaultCompassPoints.add(new CompassPoint("South West",225F));
        defaultCompassPoints.add(new CompassPoint("West",270F));
        defaultCompassPoints.add(new CompassPoint("North West",315F));

        compassPointsHashMap.put(playerUUID,defaultCompassPoints);
    }
}
