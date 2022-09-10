package me.corruptionsniper.compass.compassPoints;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PluginPlayerCompassPoints {
    private static PlayerCompassPoints playerCompassPoints;

    public PlayerCompassPoints getPlayerCompassPoints() {
        return playerCompassPoints;
    }

    public void setPlayerCompassPoints(PlayerCompassPoints playerCompassPoints) {
        if (playerCompassPoints != null) {PluginPlayerCompassPoints.playerCompassPoints = playerCompassPoints;}
        else {PluginPlayerCompassPoints.playerCompassPoints = new PlayerCompassPoints();}
    }

    public List<CompassPoint> get(Player player) {
        if (!playerCompassPoints.find(player.getUniqueId())) {
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

    public void remove(Player player, CompassPoint compassPoint) {
        List<CompassPoint> compassPointList = get(player);
        compassPointList.remove(compassPoint);
        playerCompassPoints.put(player.getUniqueId(),compassPointList);
    }

    public boolean find(Player player) {
        return playerCompassPoints.find(player.getUniqueId());
    }

    public List<CompassPoint> defaultCompassPoints() {
        List<CompassPoint> defaultCompassPoints = new ArrayList<>();
        defaultCompassPoints.add(new CompassPoint("direction","North",0F,null,null));
        defaultCompassPoints.add(new CompassPoint("direction","North East",45F,null,null));
        defaultCompassPoints.add(new CompassPoint("direction","East",90F,null,null));
        defaultCompassPoints.add(new CompassPoint("direction","South East",135F,null,null));
        defaultCompassPoints.add(new CompassPoint("direction","South",180F,null,null));
        defaultCompassPoints.add(new CompassPoint("direction","South West",225F,null,null));
        defaultCompassPoints.add(new CompassPoint("direction","West",270F,null,null));
        defaultCompassPoints.add(new CompassPoint("direction","North West",315F,null,null));
        return defaultCompassPoints;
    }
}
