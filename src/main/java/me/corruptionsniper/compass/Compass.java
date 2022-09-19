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

        //()
        float trueFov;
        if (aspectRatio == 0.5625) {
            trueFov = PolynomialFunction(fov, Arrays.asList(1.18F, 1.81F, -0.0052F));
        } else if (aspectRatio == 0.75) {
            trueFov = PolynomialFunction(fov, Arrays.asList(1.25F, 1.43F, -0.0026F));
        } else {
            trueFov = fov;
        }
        int length = ((int) (width * compassScreenCoverage)/(characterPixelWidth * guiScale) );

        /*
        Creating a map that stores the position and colour that has to be later inserted into the compass.
        This is done due to the fact that adding a ChatColour to a string shifts the string index at any point forward
        of it messing up the compass point placement, and therefore as to be added at a later time in reverse order
        which explains the use of a treemap that sorts in reverse order.
        */
        Map<Integer,ChatColor> chatColorPlacementMap = new TreeMap<>(Collections.reverseOrder());

        //Creation of the compass base.
        chatColorPlacementMap.put(0, compassBaseColour);
        StringBuilder compass = new StringBuilder();
        for (int i = 0; i < length; i++) {
            compass.append(compassBaseSymbol);
        }

        //Iterates through compass points to check if compass point is to be placed onto the compass, and if so, places it onto the compass.
        for (CompassPoint compassPoint : pluginPlayerCompassPoints.get(player)) {

            Float compassPointBearing = compassPoint.getBearing();

            //Checks the type of compass point.
            if (!compassPoint.getType().equalsIgnoreCase("direction")) {
                if (compassPoint.getType().equalsIgnoreCase("coordinate")) {
                compassPointBearing = (float) (Math.atan2( compassPoint.getXCoordinate() - player.getLocation().getX(), player.getLocation().getZ() - compassPoint.getZCoordinate()) * (360/(3.14159F * 2)));
                } else {continue;}
            }

            //Checking if compass point is in the field of view of the player.
            float difference = Modulus(compassPointBearing - playerBearing, 360);
            if (!(difference < trueFov / 2)) {
                if (difference > (360 - (trueFov / 2))) {
                    difference -= 360;
                } else {continue;}
            }

            //Placement of the compass point on compass. (The use of a cubic is to account for Minecraft FOV scaling, which stretches the screen)
            float spread = 0.5F;
            int placement = (int) (length * (0.5F * PolynomialFunction((2 * difference)/trueFov, Arrays.asList(0F, 1 - spread, 0F, spread)) + 0.5F));

            //Name of the compass point on compass.
            String[] compassPointLabelArguments = compassPoint.getLabel().split(" ",3);
            StringBuilder compassPointInitials = new StringBuilder();
            for (String args : compassPointLabelArguments) {
                compassPointInitials.append(args.charAt(0));
            }

            //Placing of compass point on compass
            compass.replace(placement,placement + compassPointLabelArguments.length, compassPointInitials.toString());

            //Adding colour and placement of compass point onto map.
            chatColorPlacementMap.put(placement, compassPoint.getColour());
            chatColorPlacementMap.put(placement + compassPointLabelArguments.length, compassBaseColour);
        }

        //Adding ChatColour to compass.
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

    private float Modulus(float a, float b) {
        return (float) (a - (Math.floor(a/b) * b));
    }
}