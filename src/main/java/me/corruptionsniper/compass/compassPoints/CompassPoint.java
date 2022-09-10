package me.corruptionsniper.compass.compassPoints;

import org.bukkit.ChatColor;

public class CompassPoint {

    String type;
    String label;
    Float bearing;
    Float xCoordinate;
    Float zCoordinate;
    ChatColor colour;

    public CompassPoint(String type, String label, Float degrees, Float xCoordinate, Float zCoordinate, ChatColor colour) {
        this.type = type;
        this.label = label;
        this.bearing = degrees;
        this.xCoordinate = xCoordinate;
        this.zCoordinate = zCoordinate;
        this.colour = colour;
    }

    public String getType() {
        return  type;
    }

    public String getLabel() {
        return label;
    }

    public Float getBearing() {
        return bearing;
    }

    public Float getXCoordinate() {
        return xCoordinate;
    }

    public Float getZCoordinate() {
        return zCoordinate;
    }

    public ChatColor getColour() {
        return colour;
    }
    public void setColour(String string) {colour = stringToChatColour(string);}

    private ChatColor stringToChatColour(String string) {
        switch (string) {
            case "black":
                return ChatColor.BLACK;
            case "dark blue":
                return ChatColor.DARK_BLUE;
            case "dark green":
                return ChatColor.DARK_GREEN;
            case "dark aqua":
                return ChatColor.DARK_AQUA;
            case "dark red":
                return ChatColor.DARK_RED;
            case "dark purple":
                return ChatColor.DARK_PURPLE;
            case "gold":
                return ChatColor.GOLD;
            case "gray":
                return ChatColor.GRAY;
            case "dark gray":
                return ChatColor.DARK_GRAY;
            case "blue":
                return ChatColor.BLUE;
            case "green":
                return ChatColor.GREEN;
            case "aqua":
                return ChatColor.AQUA;
            case "red":
                return ChatColor.RED;
            case "light purple":
                return ChatColor.LIGHT_PURPLE;
            case "yellow":
                return ChatColor.YELLOW;
            default:
                return ChatColor.WHITE;
        }
    }

}
