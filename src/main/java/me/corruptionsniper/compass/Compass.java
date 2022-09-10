package me.corruptionsniper.compass;

import me.corruptionsniper.compass.compassPoints.CompassPoint;
import me.corruptionsniper.compass.compassPoints.PluginPlayerCompassPoints;
import me.corruptionsniper.compass.settings.PluginPlayerSettings;
import me.corruptionsniper.compass.settings.Settings;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class Compass {

    PluginPlayerSettings pluginPlayerSettings = new PluginPlayerSettings();
    PluginPlayerCompassPoints pluginPlayerCompassPoints = new PluginPlayerCompassPoints();

    //The constant for the number of characters which span a screen with a GUI scale of 1.
    int characterPixelWidth = 6;
    char compassBaseSymbol = '-';
    ChatColor compassBaseColour = ChatColor.GRAY;

    public String newCompass(Player player) {
        Settings settings = pluginPlayerSettings.get(player);
        int guiScale = settings.getGuiScale();
        int fov = settings.getFov();
        int width = settings.getWidth();
        int height = settings.getHeight();
        float compassScreenCoverage = settings.getScreenCoverage();
        float playerBearing = player.getLocation().getYaw() + 180;

        float aspectRatio = ((float) height)/((float) width);
        float trueFov;
        if (aspectRatio == 0.5625) {
            trueFov = PolynomialFunction(fov, Arrays.asList(1.18F, 1.81F, -0.0052F));
        } else if (aspectRatio == 0.75) {
            trueFov = PolynomialFunction(fov, Arrays.asList(1.25F, 1.43F, -0.0026F));
        } else {
            trueFov = fov;
        }
        int length = ((int) (width * compassScreenCoverage)/(characterPixelWidth * guiScale) );

        Map<Integer,ChatColor> chatColorPlacementMap = new TreeMap<>(Collections.reverseOrder());

        chatColorPlacementMap.put(0, compassBaseColour);
        StringBuilder compass = new StringBuilder();
        for (int i = 0; i < length; i++) {
            compass.append(compassBaseSymbol);
        }

        for (CompassPoint compassPoint : pluginPlayerCompassPoints.get(player)) {
            Float compassPointBearing = compassPoint.getBearing();
            if (!compassPoint.getType().equalsIgnoreCase("direction")) {
                if (compassPoint.getType().equalsIgnoreCase("coordinate")) {
                compassPointBearing = (float) (Math.atan2( compassPoint.getXCoordinate() - player.getLocation().getX(), player.getLocation().getZ() - compassPoint.getZCoordinate()) * (360/(3.14159F * 2)));
                } else {continue;}
            }


            float difference = Modulus(compassPointBearing - playerBearing, 360);
            if (!(difference < trueFov / 2)) {
                if (difference > (360 - (trueFov / 2))) {
                    difference -= 360;
                } else {continue;}
            }

            //Placement of the compass point on compass.
            int placement = (int) (length * (((difference / trueFov) + 0.5F) % 1));

            //Name of the compass point on compass.
            String[] compassPointLabelArguments = compassPoint.getLabel().split(" ",3);
            StringBuilder compassPointInitials = new StringBuilder();
            for (String args : compassPointLabelArguments) {
                compassPointInitials.append(args.charAt(0));
            }

            //Placing of compass point on compass
            compass.replace(placement,placement + compassPointLabelArguments.length, compassPointInitials.toString());

            chatColorPlacementMap.put(placement, compassPoint.getColour());
            chatColorPlacementMap.put(placement + compassPointLabelArguments.length, compassBaseColour);
        }

        for (Map.Entry<Integer, ChatColor> entry : chatColorPlacementMap.entrySet()) {
            compass.insert(entry.getKey(), entry.getValue());
        }

        return compass.toString();
    }

    private float PolynomialFunction(float x, List<Float> coefficients) {
        float xPowerValue = 1;
        float total = 0;
        for (float coefficient : coefficients) {
            total += coefficient * xPowerValue;
            xPowerValue *= x;
        }
        return total;
    }

    private  float Modulus(float a, float b) {
        return (float) (a - (Math.floor(a/b) * b));
    }
}