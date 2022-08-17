package me.corruptionsniper.compass;

public class CompassPoints {
    String label;
    Float bearing;

    CompassPoints(String label, Float degrees) {
        this.label = label;
        this.bearing = degrees;
    }

    public String getLabel() {return label;}
    public Float getBearing() {return bearing;}
}
