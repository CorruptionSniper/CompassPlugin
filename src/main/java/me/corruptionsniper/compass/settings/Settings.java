package me.corruptionsniper.compass.settings;

public class Settings {
    private boolean compass;
    private int guiScale;
    private int fov;

    Settings(boolean compass, int guiScale, int fov) {
        this.compass = compass;
        this.guiScale = guiScale;
        this.fov = fov;
    }
    public void setCompass(boolean compass){
        this.compass = compass;
    }
    public boolean getCompass() {
        return compass;
    }
}
