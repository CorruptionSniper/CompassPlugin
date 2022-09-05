package me.corruptionsniper.compass;

import me.corruptionsniper.compass.compassPoints.CompassPoint;
import me.corruptionsniper.compass.compassPoints.PluginPlayerCompassPoints;
import me.corruptionsniper.compass.settings.PluginPlayerSettings;
import me.corruptionsniper.compass.settings.Settings;
import org.bukkit.entity.Player;

public class Compass {

    PluginPlayerSettings pluginPlayerSettings = new PluginPlayerSettings();
    PluginPlayerCompassPoints pluginPlayerCompassPoints = new PluginPlayerCompassPoints();

    //The constant for the number of characters which span a screen with a GUI scale of 1.
    int characterPixelWidth = 6;


    public String newCompass(Player player) {
        Settings settings = pluginPlayerSettings.get(player);
        int guiScale = settings.getGuiScale();
        int fov = settings.getFov();
        int width = settings.getWidth();
        int height = settings.getHeight();
        float compassScreenCoverage = settings.getScreenCoverage();
        float bearing = player.getLocation().getYaw() + 180;

        float aspectRatio = ((float) height)/((float) width);
        float trueFov;
        if (aspectRatio == 0.5625) {
            trueFov = (-0.0052F * fov * fov) + (1.81F * fov) + 1.18F;
        } else if (aspectRatio == 0.75) {
            trueFov = (-0.0026F * fov * fov) + (1.43F * fov) - 1.25F;
        } else {
            trueFov = fov;
        }

        int length = ((int) (width * compassScreenCoverage)/(characterPixelWidth * guiScale) );

        StringBuilder compass = new StringBuilder();
        for (int i = 0; i < length; i++) {
            compass.append('-');
        }

        for (CompassPoint compassPoint : pluginPlayerCompassPoints.get(player)) {
            float compassPointBearing = compassPoint.getBearing();
            if (compassPointBearing < (bearing + trueFov/2F) & compassPointBearing > (bearing - trueFov/2F)) {
                String[] compassPointLabelArguments = compassPoint.getLabel().split(" ",3);

                StringBuilder compassPointInitials = new StringBuilder();
                for (String args : compassPointLabelArguments) {
                    compassPointInitials.append(args.charAt(0));
                }

                int placement = (int) ((length * ((compassPointBearing)-(bearing - trueFov/2F)))/(trueFov));

                compass.replace(placement,placement + compassPointLabelArguments.length, compassPointInitials.toString());
            }
        }
        return compass.toString();
    }
}