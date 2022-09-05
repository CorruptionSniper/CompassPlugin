package me.corruptionsniper.compass.settings;

public class Settings {
    //Toggle for the compass
    private boolean compass;
    //The size of the player's GUI
    private int guiScale;
    //The player's options Field Of View.
    private int fov;
    //% size of the compass section (Where at 100% the compass section spans across the whole screen).
    float screenCoverage;
    //Resolution of the player's screen
    int width;
    int height;

    public Settings(boolean compass, int guiScale, int fov, float screenCoverage,int width, int height) {
        this.compass = compass;
        this.guiScale = guiScale;
        this.fov = fov;
        this.screenCoverage = screenCoverage;
        this.width = width;
        this.height = height;
    }

    public boolean getCompass() {
        return compass;
    }
    public void setCompass(boolean compass) {
        this.compass = compass;
    }

    public int getGuiScale() {
        return guiScale;
    }
    public void setGuiScale(int guiScale) {
        this.guiScale = guiScale;
    }

    public int getFov() {
        return fov;
    }
    public void setFov(int fov) {
        this.fov = fov;
    }

    public float getScreenCoverage() {
        return screenCoverage;
    }
    public void setScreenCoverage(float screenCoverage) {
        this.screenCoverage = screenCoverage;
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
}
