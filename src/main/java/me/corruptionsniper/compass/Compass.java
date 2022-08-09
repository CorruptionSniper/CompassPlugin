package me.corruptionsniper.compass;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class Compass {
    //A StringBuilder which stores the compass.
    StringBuilder compass = new StringBuilder();
    //A HashMap which stores the compass points, and their bearing.
    HashMap<String, Float> compassPoints = new HashMap<>();

    //Constant for n of characters which span a screen (with a GUI scale of 3).
    int screenWidth = 102;
    //% size of the compass section (Where at 100% the compass section spans across the whole screen).

    float compassScreenCoverage = 1;
    //Player in-game Field Of View.
    int fov = 95;

    //Returns the compass.
    public String PlayerCompass(Player player) {

        //Calculation of the character length of the compass.
        int length = screenWidth * 360/fov;

        //Base compass directions are put inside a hashmap.
        compassPoints.put("North",0F);
        compassPoints.put("North East",45F);
        compassPoints.put("East",90F);
        compassPoints.put("South East",135F);
        compassPoints.put("South",180F);
        compassPoints.put("South West",225F);
        compassPoints.put("West",270F);
        compassPoints.put("North West",315F);

        //Creates the base for the compass.
        for (int i = 0; i < length; i++) {
            compass.append("-");
        }

        //Iterates through the HashMap's keys, adding all the compass points to the compass.
        for (String compassPointLabel : compassPoints.keySet()) {

            //Splits the compass point's label into arguments inside a string list.
            String[] compassPointLabelArguments = compassPointLabel.split(" ",3);
            //Iterates through the arguments of the compass point's label, adding the initial of each argument to a string.
            String compassPointInitials = "";
            for (String args : compassPointLabelArguments) {
                compassPointInitials += args.charAt(0);
            }

            //Fetching of the compass point's bearing.
            float compassPointBearing = compassPoints.get(compassPointLabel);
            //Calculation of the index where the compass point be placed on the compass.
            int placement = (int) (length * compassPointBearing/360);

            //Places the initials of the compass point on the compass.
            compass.replace(placement,placement + compassPointLabelArguments.length, compassPointInitials);
        }
        return compass.toString();
    }

    //Returns the compass section (The portion of the compass which fits the Field Of View of the player).
    public String PlayerCompassSection(Player player) {
        //Calculation of the character length of the compass.
        int length = screenWidth * 360/fov;
        //Conversion of player's yaw into a bearing ranging from 0 to 360.
        float bearing = player.getLocation().getYaw() + 180;

        String compass = PlayerCompass(player);

        //Writes the compass three times in a string so that in the compass section creation the substring does not go outside the index range.
        String loopingCompass = "";
        for (int i = 0; i < 3 ;i++) {
            loopingCompass += compass;
        }

        //Creates a substring of the compass by using the index on the string equivalent to the bearing of the player ± half of the screen width, resized by the compass screen coverage to determine the size of the compass on the screen in-game.
        String compassSection = loopingCompass.substring((int) (length + ((bearing - (screenWidth * compassScreenCoverage)/2) * length)/360), (int) (length + ((bearing + (screenWidth * compassScreenCoverage)/2) * length)/360));

        //-Debug-
        System.out.println(compassSection);

        //Returns the compass section (The portion of the compass which fits the Field Of View of the player).
        return compassSection;
    }
}