package me.corruptionsniper.compass.compassPoints;

import org.bukkit.ChatColor;

public class CompassPoint implements Comparable<CompassPoint>{

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
    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public Float getBearing() {
        return bearing;
    }
    public void setBearing(Float bearing) {
        this.bearing = bearing;
    }

    public Float getXCoordinate() {
        return xCoordinate;
    }
    public void setXCoordinate(Float xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Float getZCoordinate() {
        return zCoordinate;
    }
    public void setZCoordinate(Float zCoordinate) {
        this.zCoordinate = zCoordinate;
    }

    public ChatColor getColour() {
        return colour;
    }
    public void setColour(String string) {
        this.colour = stringToChatColour(string);
    }

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

    @Override
    public int compareTo(CompassPoint anotherCompassPoint) {
        return label.compareTo(anotherCompassPoint.getLabel());
    }

    @Override
    public boolean equals(Object anotherObject){
        if (anotherObject instanceof CompassPoint) {
            CompassPoint anotherCompassPoint = (CompassPoint) anotherObject;
            return label.equals(anotherCompassPoint.getLabel());
        }
        if (anotherObject instanceof String) {
            String anotherString = (String) anotherObject;
            return label.equals(anotherString);
        }
        return false;
    }
}
