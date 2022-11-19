package me.corruptionsniper.compass;

import me.corruptionsniper.compass.compassPoints.CompassPoint;
import me.corruptionsniper.compass.compassPoints.PlayerCompassPoints;
import me.corruptionsniper.compass.settings.PlayerSettings;
import me.corruptionsniper.compass.settings.Settings;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class Compass {

    private static final int CHAR_WIDTH_IN_PIXELS = 6;
    private static final char COMPASS_BASE_SYMBOL = '-';
    private static final int COMPASS_POINT_MAX_LENGTH = 3;
    private static final float ASPECT_RATIO_16by9 = 0.5625F;
    private static final float ASPECT_RATIO_4by3 = 0.75F;

    private static final PlayerCompassPoints playerCompassPoints = new PlayerCompassPoints();
    private static final PlayerSettings playerSettings = new PlayerSettings();

    Player player;
    public Compass(Player player) {
        this.player = player;
    }

    private final ChatColor compassBaseColour = ChatColor.GRAY;

    /*
    Creating a map that stores the position and colour that has to be later inserted into the compass.
    This is done due to the fact that adding a ChatColour to a string shifts the string index at any point forward
    of it messing up the compass point placement, and therefore it has to be added at a later time in reverse order
    which explains the use of a treemap that sorts in reverse order.
    */
    private Map<Integer,ChatColor> colourIndexMap;

    private StringBuilder compass = new StringBuilder();

    public String compassGenerator() {
        Settings settings = playerSettings.get(player);
        int compassLength = getCompassLength(settings);
        emptyColorIndexMap();

        generateCompassBase(compassLength);
        insertCompassPoints(compassLength, settings);
        addAllColoursToCompass();

        return compass.toString();
    }

    //Calculates the length of the compass proportionally to the width of the user's screen,
    //the percentage of the screen it should cover, and inversely proportionally to the GUI scale.
    private int getCompassLength(Settings settings) {
        float guiScale = settings.getGuiScale();
        float compassScreenCoverage = settings.getScreenCoverage();
        return (int) ((settings.getWidth() * compassScreenCoverage)/(CHAR_WIDTH_IN_PIXELS * guiScale));
    }

    //The in-game Field Of View gets altered by the aspect ratio of the user's screen, therefore it has to be adjusted.
    private float getCorrectFOV(Settings settings) {
        final float aspectRatio = ((float) settings.getHeight())/((float)settings.getWidth());

        float fov = settings.getFov();

        //The equations below model how the fov gets altered at an aspect ratio of 16:9 and 4:3, no other aspect ratios
        //are currently supported.
        if (aspectRatio == ASPECT_RATIO_16by9) {
            fov = polynomialFunction(fov, new float[]{1.18F, 1.81F, -0.0052F});
        } else if (aspectRatio == ASPECT_RATIO_4by3) {
            fov = polynomialFunction(fov, new float[]{1.25F, 1.43F, -0.0026F});
        }
        return fov;
    }

    private void emptyColorIndexMap() {
        colourIndexMap = new TreeMap<>(Collections.reverseOrder());
    }

    private void generateCompassBase(int length) {
        StringBuilder generatedCompass = new StringBuilder(length);
        insertColourInMap(0, compassBaseColour);
        for (int i = 0; i < length; i++) {
            generatedCompass.append(COMPASS_BASE_SYMBOL);
        }
        compass = generatedCompass;
    }

    private void insertColourInMap(int index, ChatColor color) {
        colourIndexMap.put(index, color);
    }

    //Iterates through compass points to check if compass point is to be placed onto the compass, and if so, places it onto the compass.
    private void insertCompassPoints(int length, Settings settings) {
        float playerFov = getCorrectFOV(settings);
        float playerBearing = player.getLocation().getYaw() + 180;
        for (CompassPoint compassPoint : playerCompassPoints.get(player)) {

            float relativeBearing = getPlayerRelativeBearing(playerFov, playerBearing, compassPoint);

            boolean isCompassPointInView = !(relativeBearing > playerFov / 2);
            if (isCompassPointInView) {
                int indexOfCompassPoint = getPlacement(length, playerFov, relativeBearing);
                String compassPointName = generateCompassPointName(compassPoint);

                insertCompassPoint(indexOfCompassPoint, compassPointName);
                insertCompassPointColour(compassPoint, indexOfCompassPoint, compassPointName);
            }
        }
    }

    //Checking if compass point is in the field of view of the player.
    private float getPlayerRelativeBearing(float fov, float playerBearing, CompassPoint compassPoint) {
        final int fullAngle = 360;
        float compassPointBearing = getCompassPointBearing(compassPoint);
        float playerRelativeBearing = modulus(compassPointBearing - playerBearing, fullAngle);
        if (playerRelativeBearing > fullAngle - (fov / 2)) {
            playerRelativeBearing -= fullAngle;
        } //else playerRelativeBearing remains unmodified
        return playerRelativeBearing;
    }

    //Placement of the compass point on compass. (The use of a cubic is to account for Minecraft FOV scaling, which stretches the screen)
    private int getPlacement(int length, float fov, float difference) {
        final float spread = 0.5F;
        float fovStretchingModelFunction = 0.5F * polynomialFunction((2 * difference)/ fov, new float[]{0F, 1 - spread, 0F, spread});
        float percentagePlacement = fovStretchingModelFunction + 0.5F;
        return (int) (length * percentagePlacement);
    }

    //Adding colour and index of compass point onto map.
    private void insertCompassPointColour(CompassPoint compassPoint, int index, String compassPointName) {
        insertColourInMap(index, compassPoint.getColour());
        insertColourInMap(index + compassPointName.length(), compassBaseColour);
    }

    //Placing of compass point on compass
    private void insertCompassPoint(int placement, String compassPointName) {
        compass.replace(placement, placement + compassPointName.length(), compassPointName);
    }

    //Name of the compass point on compass.
    private String generateCompassPointName(CompassPoint compassPoint) {
        StringBuilder compassPointInitials = new StringBuilder(COMPASS_POINT_MAX_LENGTH);
        String[] compassPointLabelArguments = compassPoint.getLabel().split(" ",COMPASS_POINT_MAX_LENGTH);
        for (String args : compassPointLabelArguments) {
            compassPointInitials.append(args.charAt(0));
        }
        return compassPointInitials.toString();
    }

    //Returns the bearing of the compassPoint in relation to the player according to the type of compass point.
    private float getCompassPointBearing(CompassPoint compassPoint) {
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
                throw new RuntimeException("Unknown Compass Type");
        }
        return bearing;
    }

    private void addAllColoursToCompass() {
        for (Map.Entry<Integer, ChatColor> entry : colourIndexMap.entrySet()) {
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