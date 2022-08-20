package me.corruptionsniper.compass;

class Settings {
    private boolean compass;

    Settings(boolean compass) {
        this.compass = compass;
    }
    public void setCompass(boolean compass){this.compass = compass;}
    public boolean getCompass() {
        return compass;
    }
}
