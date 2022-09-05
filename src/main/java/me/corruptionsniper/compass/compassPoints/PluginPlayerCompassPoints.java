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
        defaultCompassPoints.add(new CompassPoint("North", 0F));
        defaultCompassPoints.add(new CompassPoint("North East",45F));
        defaultCompassPoints.add(new CompassPoint("East",90F));
        defaultCompassPoints.add(new CompassPoint("South East",135F));
        defaultCompassPoints.add(new CompassPoint("South",180F));
        defaultCompassPoints.add(new CompassPoint("South West",225F));
        defaultCompassPoints.add(new CompassPoint("West",270F));
        defaultCompassPoints.add(new CompassPoint("North West",315F));
        return defaultCompassPoints;
    }
}
