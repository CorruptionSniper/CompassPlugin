package me.corruptionsniper.compass.compassPoints;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PluginPlayerCompassPoints {
    private static HashMap<UUID, List<CompassPoint>> playerCompassPoints = new HashMap<>();

    public HashMap<UUID, List<CompassPoint>> getPlayerCompassPoints() {
        return playerCompassPoints;
    }

    public void setPlayerCompassPoints(HashMap<UUID, List<CompassPoint>> playerCompassPoints) {
        if (playerCompassPoints != null) {PluginPlayerCompassPoints.playerCompassPoints = playerCompassPoints;}
        else {PluginPlayerCompassPoints.playerCompassPoints = new HashMap<>();}
    }

    public List<CompassPoint> get(Player player) {
        if (!playerCompassPoints.containsKey(player.getUniqueId())) {
            playerCompassPoints.put(player.getUniqueId(),defaultCompassPoints());
        }
        return playerCompassPoints.get(player.getUniqueId());
    }

    public void put(Player player, List<CompassPoint> compassPointList) {
        playerCompassPoints.put(player.getUniqueId(), compassPointList);
    }

    public void putCompassPoint(Player player, CompassPoint compassPoint) {
        List<CompassPoint> compassPointsList = get(player);
        compassPointsList.add(compassPoint);
        playerCompassPoints.put(player.getUniqueId(), compassPointsList);
    }

    public boolean remove(Player player, CompassPoint compassPoint) {
        return playerCompassPoints.get(player.getUniqueId()).remove(compassPoint);
    }

    public List<CompassPoint> defaultCompassPoints() {
        List<CompassPoint> defaultCompassPoints = new ArrayList<>();
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
