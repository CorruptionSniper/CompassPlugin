package me.corruptionsniper.compass.compassPoints;

import org.bukkit.entity.Player;

import java.util.List;

public class PluginPlayerCompassPoints {
    private static PlayerCompassPoints playerCompassPoints;

    public PlayerCompassPoints getPlayerCompassPoints() {
        return playerCompassPoints;
    }
    public void setPlayerCompassPoints(PlayerCompassPoints playerCompassPoints) {
        PluginPlayerCompassPoints.playerCompassPoints = playerCompassPoints;
    }
    public List<CompassPoint> get(Player player) {
        return playerCompassPoints.get(player.getUniqueId());
    }
    public void put(Player player, List<CompassPoint> compassPointList) {
        playerCompassPoints.put(player.getUniqueId(), compassPointList);
    }
    public void putCompassPoint(Player player, CompassPoint compassPoint) {
        playerCompassPoints.putCompassPoint(player.getUniqueId(),compassPoint);
    }
    public void remove(Player player, CompassPoint compassPoint) {
        playerCompassPoints.remove(player.getUniqueId(),compassPoint);
    }
    public boolean find(Player player) {
        return playerCompassPoints.find(player.getUniqueId());
    }
    public void restoreDefaults(Player player) {
        playerCompassPoints.restoreDefaults(player.getUniqueId());
    }
}
