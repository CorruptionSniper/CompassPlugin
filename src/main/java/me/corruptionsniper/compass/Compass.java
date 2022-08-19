package me.corruptionsniper.compass;

import org.bukkit.entity.Player;

import java.util.List;

public class Compass {
    //Constant for n of characters which span a screen (with a GUI scale of 3).
    int screenWidth = 102;
    //% size of the compass section (Where at 100% the compass section spans across the whole screen).
    float compassScreenCoverage = 1;
    //Player in-game Field Of View.
    int fov = 95;

    //Returns the compass.
    public String PlayerCompass(Player player) {
        //A StringBuilder which stores the compass.
        StringBuilder compass = new StringBuilder();

        //Calculation of the character length of the compass.
        int length = screenWidth * 360/fov;

        PluginPlayerCompassPoints pluginPlayerCompassPoints = new PluginPlayerCompassPoints();
        List<CompassPoints> compassPointsList = pluginPlayerCompassPoints.get(player);

        if (!pluginPlayerCompassPoints.find(player)) {
            compassPointsList = pluginPlayerCompassPoints.restoreDefaults(player);
        }

        //Creates the base for the compass.
        for (int i = 0; i < length; i++) {
            compass.append("-");
        }

        //Iterates through the HashMap's keys, adding all the compass points to the compass.
        for (CompassPoints compassPoint : compassPointsList) {

            //Splits the compass point's label into arguments inside a string list.
            String[] compassPointLabelArguments = compassPoint.getLabel().split(" ",3);
            //Iterates through the arguments of the compass point's label, adding the initial of each argument to a string.
            StringBuilder compassPointInitials = new StringBuilder();
            for (String args : compassPointLabelArguments) {
                compassPointInitials.append(args.charAt(0));
            }

            //Fetching of the compass point's bearing.
            float compassPointBearing = compassPoint.getBearing();
            //Calculation of the index where the compass point be placed on the compass.
            int placement = (int) (length * compassPointBearing/360);

            //Places the initials of the compass point on the compass.
            compass.replace(placement,placement + compassPointLabelArguments.length, compassPointInitials.toString());
        }
        return compass.toString();
    }

    //Returns the compass section (The portion of the compass which fits the Field Of View of the player).
    public String PlayerCompassSection(Player player) {
        //Creation of the player's compass to manipulate.
        String compass = PlayerCompass(player);

        //The compass' length.
        int length = compass.length();
        //Conversion of player's yaw into a bearing ranging from 0 to 360.
        float bearing = player.getLocation().getYaw() + 180;

        //Writes the compass three times in a string so that in the compass section creation the substring does not go outside the index range.
        StringBuilder loopingCompass = new StringBuilder();
        for (int i = 0; i < 3 ;i++) {
            loopingCompass.append(compass);
        }

        //Creates a substring of the compass by using the index on the string equivalent to the bearing of the player ± half of the screen width, resized by the compass screen coverage to determine the size of the compass on the screen in-game.
        String compassSection = loopingCompass.substring((int) (length + ((bearing - (screenWidth * compassScreenCoverage)/2) * length)/360), (int) (length + ((bearing + (screenWidth * compassScreenCoverage)/2) * length)/360));

        //-Debug-
        System.out.println(compassSection);

        //Returns the compass section (The portion of the compass which fits the Field Of View of the player).
        return compassSection;
    }
}