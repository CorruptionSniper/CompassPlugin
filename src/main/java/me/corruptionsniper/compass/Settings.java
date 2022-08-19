package me.corruptionsniper.compass;

class Settings {
    boolean compass;

    Settings(boolean compass) {
        this.compass = compass;
    }

    public void setSettings(boolean compass) {
        this.compass = compass;
    }
    public boolean getCompass() {
        return compass;
    }
}
