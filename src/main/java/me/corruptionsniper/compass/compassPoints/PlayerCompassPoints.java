package me.corruptionsniper.compass.compassPoints;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerCompassPoints {
    private static HashMap<UUID, CompassPointSet> playerCompassPoints = new HashMap<>();

    public HashMap<UUID, CompassPointSet> getPlayerCompassPoints() {
        return playerCompassPoints;
    }

    public CompassPointSet get(Player player) {
        if (!playerCompassPoints.containsKey(player.getUniqueId())) {
            playerCompassPoints.put(player.getUniqueId(),defaultCompassPoints());
        }
        return playerCompassPoints.get(player.getUniqueId());
    }

    public void put(Player player, CompassPointSet compassPointList) {
        playerCompassPoints.put(player.getUniqueId(), compassPointList);
    }

    public void putCompassPoint(Player player, CompassPoint compassPoint) {
        CompassPointSet compassPointsList = get(player);
        compassPointsList.remove(compassPoint);
        compassPointsList.add(compassPoint);
        playerCompassPoints.put(player.getUniqueId(), compassPointsList);
    }

    //Refactor
    public CompassPoint getCompassPoint(Player player, Object object) {
        return playerCompassPoints.get(player.getUniqueId()).get(object);
    }

    public boolean removeCompassPoint(Player player, Object object) {
        return playerCompassPoints.get(player.getUniqueId()).remove(object);
    }

    public CompassPointSet defaultCompassPoints() {
        CompassPointSet defaultCompassPoints = new CompassPointSet();
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
