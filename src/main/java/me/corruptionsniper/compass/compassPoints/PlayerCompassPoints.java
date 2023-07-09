package me.corruptionsniper.compass.compassPoints;

import me.corruptionsniper.compass.PlayerMap;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.UUID;

public class PlayerCompassPoints extends PlayerMap<TreeSet<CompassPoint>> {

    private static HashMap<UUID, TreeSet<CompassPoint>> playerCompassPointsMap = new HashMap<>();

    @Override
    public String getFileName() {
        return "compassPoints.json";
    }

    @Override
    public HashMap<UUID, TreeSet<CompassPoint>> getMap() {
        return playerCompassPointsMap;
    }

    @Override
    public void setMap(HashMap<UUID, TreeSet<CompassPoint>> dataMap) {
        if (dataMap != null) playerCompassPointsMap = dataMap;
        else System.out.println("dataMap is null!");
    }

    public TreeSet<CompassPoint> get(UUID playerUUID) {
        return super.get(playerUUID, privateGet(playerUUID));
    }

    private TreeSet<CompassPoint> privateGet(UUID playerUUID) {
        return playerCompassPointsMap.get(playerUUID);
    }

    @Override
    public TreeSet<CompassPoint> put(UUID playerUUID, TreeSet<CompassPoint> value) {
        return playerCompassPointsMap.put(playerUUID,value);
    }

    @Override
    public TreeSet<CompassPoint> restoreDefaults(UUID playerUUID) {
        return super.restoreDefaults(playerUUID);
    }

    @Override
    public TreeSet<CompassPoint> defaults() {
        TreeSet<CompassPoint> defaultsCompassPoints = new TreeSet<>();
        defaultsCompassPoints.add(new CompassPoint("East","bearing", 90F, null, null, ChatColor.WHITE));
        defaultsCompassPoints.add(new CompassPoint("North","bearing", 0F, null, null, ChatColor.WHITE));
        defaultsCompassPoints.add(new CompassPoint("North East","bearing", 45F, null, null, ChatColor.WHITE));
        defaultsCompassPoints.add(new CompassPoint("North West","bearing", 315F, null, null, ChatColor.WHITE));
        defaultsCompassPoints.add(new CompassPoint("South","bearing", 180F, null, null, ChatColor.WHITE));
        defaultsCompassPoints.add(new CompassPoint("South East","bearing", 135F, null, null, ChatColor.WHITE));
        defaultsCompassPoints.add(new CompassPoint("South West","bearing", 225F, null, null, ChatColor.WHITE));
        defaultsCompassPoints.add(new CompassPoint("West","bearing", 270F, null, null, ChatColor.WHITE));
        return defaultsCompassPoints;
    }
}
