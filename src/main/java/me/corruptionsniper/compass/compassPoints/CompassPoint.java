package me.corruptionsniper.compass.compassPoints;

public class CompassPoint {

    String type;
    String label;
    Float bearing;
    Float xCoordinate;
    Float zCoordinate;

    public CompassPoint(String type, String label, Float degrees, Float xCoordinate, Float zCoordinate) {
        this.type = type;
        this.label = label;
        this.bearing = degrees;
        this.xCoordinate = xCoordinate;
        this.zCoordinate = zCoordinate;
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
}
