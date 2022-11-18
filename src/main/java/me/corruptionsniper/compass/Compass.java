package me.corruptionsniper.compass;

import me.corruptionsniper.compass.compassPoints.CompassPoint;
import me.corruptionsniper.compass.compassPoints.PlayerCompassPoints;
import me.corruptionsniper.compass.settings.PlayerSettings;
import me.corruptionsniper.compass.settings.Settings;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class Compass {



    Player player;
    public Compass(Player player) {
        this.player = player;
    }

    PlayerCompassPoints playerCompassPoints = new PlayerCompassPoints();
    PlayerSettings playerSettings = new PlayerSettings();

    int characterWidthInPixels = 6;
    char compassBaseSymbol = '-';
    ChatColor compassBaseColour = ChatColor.GRAY;

    /*
    Creating a map that stores the position and colour that has to be later inserted into the compass.
    This is done due to the fact that adding a ChatColour to a string shifts the string index at any point forward
    of it messing up the compass point placement, and therefore it has to be added at a later time in reverse order
    which explains the use of a treemap that sorts in reverse order.
    */
    private Map<Integer,ChatColor> chatColorPlacementMap = new TreeMap<>(Collections.reverseOrder());

    StringBuilder compass = new StringBuilder();

    public String compassGenerator() {
        Settings settings = playerSettings.get(player);

        int compassLength = getCompassLength(settings);
        float fov = getCorrectFOV(settings);

        generateCompassBase(compassLength);
        insertCompassPoints(compassLength, fov);
        insertColour();

        return compass.toString();
    }

    private int getCompassLength(Settings settings) {
        float guiScale = settings.getGuiScale();
        float compassScreenCoverage = settings.getScreenCoverage();
        return (int) ((settings.getWidth() * compassScreenCoverage)/(characterWidthInPixels * guiScale));
    }

    //The in-game fov gets altered by the aspect ratio of the user's screen.
    //The equations below model how the fov gets altered at an aspect ratio of 16:9 and 4:3, no other aspect ratios
    //are currently supported.
    private float getCorrectFOV(Settings settings) {
        final float aspectRatio16by9 = 0.5625F;
        final float aspectRatio4by3 = 0.75F;
        final float aspectRatio = ((float) settings.getHeight())/((float)settings.getWidth());

        int fov = settings.getFov();

        float correctedFOV = 0;
        if (aspectRatio == aspectRatio16by9) {
            correctedFOV = polynomialFunction(fov, new float[]{1.18F, 1.81F, -0.0052F});
        } else if (aspectRatio == aspectRatio4by3) {
            correctedFOV = polynomialFunction(fov, new float[]{1.25F, 1.43F, -0.0026F});
        }
        return correctedFOV;
    }

    private void generateCompassBase(int length) {
        compass = new StringBuilder();
        chatColorPlacementMap.put(0, compassBaseColour);
        for (int i = 0; i < length; i++) {
            compass.append(compassBaseSymbol);
        }
    }

    private void insertCompassPoints(int length, float fov) {
        //Iterates through compass points to check if compass point is to be placed onto the compass, and if so, places it onto the compass.
        float playerBearing = player.getLocation().getYaw() + 180;
        for (CompassPoint compassPoint : playerCompassPoints.get(player)) {

            float compassPointBearing = 0;
            try {
                compassPointBearing = compassPointBearing(compassPoint);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Checking if compass point is in the field of view of the player.

            float playerRelativeBearing = modulus(compassPointBearing - playerBearing, 360);
            boolean isCompassPointInView = true;
            if (!(playerRelativeBearing < fov / 2)) {
                if (playerRelativeBearing > 360 - (fov / 2)) {
                    playerRelativeBearing -= 360;
                } else {isCompassPointInView = false;}
            }
            /*
            Needs To be Tested:
            boolean isCompassPointInView = true;
            if (playerRelativeBearing > 360 - (fov / 2)) {
                playerRelativeBearing -= 360;
            } else if (!(playerRelativeBearing < fov / 2)) {
                isCompassPointInView = false;
            }
            * */

            if (isCompassPointInView) {
                int placement = getPlacement(length, fov, playerRelativeBearing);

                String compassPointName = generateCompassPointName(compassPoint);

                insertCompassPoint(placement, compassPointName);
                colourPlacement(compassPoint, placement, compassPointName);
            }
        }
    }

    //Placement of the compass point on compass. (The use of a cubic is to account for Minecraft FOV scaling, which stretches the screen)
    private int getPlacement(int length, float fov, float difference) {
        final float spread = 0.5F;
        float fovStretchingFunction = 0.5F * polynomialFunction((2 * difference)/ fov, new float[]{0F, 1 - spread, 0F, spread});
        float percentagePlacement = fovStretchingFunction + 0.5F;
        return (int) (length * percentagePlacement);
    }

    //Adding colour and placement of compass point onto map.
    private void colourPlacement(CompassPoint compassPoint, int placement, String compassPointName) {
        chatColorPlacementMap.put(placement, compassPoint.getColour());
        chatColorPlacementMap.put(placement + compassPointName.length(), compassBaseColour);
    }

    //Placing of compass point on compass
    private void insertCompassPoint(int placement, String compassPointName) {
        compass.replace(placement, placement + compassPointName.length(), compassPointName);
    }

    //Name of the compass point on compass.
    private String generateCompassPointName(CompassPoint compassPoint) {
        final int MAX_CHAR = 3;
        StringBuilder compassPointInitials = new StringBuilder(MAX_CHAR);
        String[] compassPointLabelArguments = compassPoint.getLabel().split(" ",MAX_CHAR);
        for (String args : compassPointLabelArguments) {
            compassPointInitials.append(args.charAt(0));
        }
        return compassPointInitials.toString();
    }

    //Returns the bearing of the compassPoint in relation to the player according to the type of compass point.
    private float compassPointBearing(CompassPoint compassPoint) throws Exception {
        float bearing;
        switch (compassPoint.getType()) {
            case "direction":
                bearing = compassPoint.getBearing();
                break;
            case "coordinate":
                double dX = compassPoint.getXCoordinate() - player.getLocation().getX();
                double dZ = player.getLocation().getZ() - compassPoint.getZCoordinate();
                final float fromRadiansToDegrees = 57.2957795131F;
                bearing = (float) (Math.atan2(dX, dZ) * fromRadiansToDegrees);
                break;
            default:
                throw new Exception("Unknown Compass Point Type");
        }
        return bearing;
    }

    private void insertColour() {
        for (Map.Entry<Integer, ChatColor> entry : chatColorPlacementMap.entrySet()) {
            compass.insert(entry.getKey(), entry.getValue());
        }
    }

    private float polynomialFunction(float x, float[] coefficients) {
        float xTermValue = 1;
        float total = 0;
        for (float coefficient : coefficients) {
            total += coefficient * xTermValue;
            xTermValue *= x;
        }
        return total;
    }

    private float modulus(float a, float b) {
        return (float) (a - (Math.floor(a/b) * b));
    }
}