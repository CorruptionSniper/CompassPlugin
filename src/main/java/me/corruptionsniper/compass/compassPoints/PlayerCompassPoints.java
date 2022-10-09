package me.corruptionsniper.compass.compassPoints;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerCompassPoints {
    private static HashMap<UUID, TreeSet<CompassPoint>> playerCompassPoints = new HashMap<>();

    public HashMap<UUID, TreeSet<CompassPoint>> getPlayerCompassPoints() {
        return playerCompassPoints;
    }

    public TreeSet<CompassPoint> get(Player player) {
        if (!playerCompassPoints.containsKey(player.getUniqueId())) {
            playerCompassPoints.put(player.getUniqueId(),defaultCompassPoints());
        }
        return playerCompassPoints.get(player.getUniqueId());
    }

    public void put(Player player, TreeSet<CompassPoint> compassPointList) {
        playerCompassPoints.put(player.getUniqueId(), compassPointList);
    }

    public void putCompassPoint(Player player, CompassPoint compassPoint) {
        TreeSet<CompassPoint> compassPointsList = get(player);
        compassPointsList.remove(compassPoint);
        compassPointsList.add(compassPoint);
        playerCompassPoints.put(player.getUniqueId(), compassPointsList);
    }

    public boolean removeCompassPoint(Player player, CompassPoint compassPoint) {
        return playerCompassPoints.get(player.getUniqueId()).remove(compassPoint);
    }

    public TreeSet<CompassPoint> defaultCompassPoints() {
        TreeSet<CompassPoint> defaultCompassPoints = new TreeSet<>();
        defaultCompassPoints.add(new CompassPoint("direction","North",0F,null,null, ChatColor.WHITE));
        defaultCompassPoints.add(new CompassPoint("direction","North East",45F,null,null, ChatColor.WHITE));
        defaultCompassPoints.add(new CompassPoint("direction","East",90F,null,null, ChatColor.WHITE));
        defaultCompassPoints.add(new CompassPoint("direction","South East",135F,null,null, ChatColor.WHITE));
        defaultCompassPoints.add(new CompassPoint("direction","South",180F,null,null, ChatColor.WHITE));
        defaultCompassPoints.add(new CompassPoint("direction","South West",225F,null,null, ChatColor.WHITE));
        defaultCompassPoints.add(new CompassPoint("direction","West",270F,null,null, ChatColor.WHITE));
        defaultCompassPoints.add(new CompassPoint("direction","North West",315F,null,null, ChatColor.WHITE));
        return defaultCompassPoints;
    }
}
