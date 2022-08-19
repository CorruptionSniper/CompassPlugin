package me.corruptionsniper.compass;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PluginPlayerCompassPoints {
    private static PlayerCompassPoints playerCompassPoints;

    public PlayerCompassPoints getPlayerCompassPoints() {
        return playerCompassPoints;
    }
    public void setPlayerCompassPoints(PlayerCompassPoints playerCompassPoints) {
        PluginPlayerCompassPoints.playerCompassPoints = playerCompassPoints;
    }
    public List<CompassPoints> get(Player player) {
        return playerCompassPoints.get(player.getUniqueId());
    }
    public void put(Player player, List<CompassPoints> compassPointsList) {
        playerCompassPoints.put(player.getUniqueId(),compassPointsList);
    }
    public List<CompassPoints> restoreDefaults(Player player) {
        List<CompassPoints> defaultCompassPoints = new ArrayList<>();
        defaultCompassPoints.add(new CompassPoints("North", 0F));
        defaultCompassPoints.add(new CompassPoints("North East",45F));
        defaultCompassPoints.add(new CompassPoints("East",90F));
        defaultCompassPoints.add(new CompassPoints("South East",135F));
        defaultCompassPoints.add(new CompassPoints("South",180F));
        defaultCompassPoints.add(new CompassPoints("South West",225F));
        defaultCompassPoints.add(new CompassPoints("West",270F));
        defaultCompassPoints.add(new CompassPoints("North West",315F));
        playerCompassPoints.put(player.getUniqueId(),defaultCompassPoints);
        return defaultCompassPoints;
    }
    public boolean find(Player player) {return playerCompassPoints.find(player.getUniqueId());}

}
