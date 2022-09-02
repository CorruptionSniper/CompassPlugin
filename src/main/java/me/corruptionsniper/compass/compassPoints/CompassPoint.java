package me.corruptionsniper.compass.compassPoints;

public class CompassPoint {

    String label;
    Float bearing;

    public CompassPoint(String label, Float degrees) {
        this.label = label;
        this.bearing = degrees;
    }

    public String getLabel() {return label;}
    public Float getBearing() {return bearing;}
}
