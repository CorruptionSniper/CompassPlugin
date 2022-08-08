package me.corruptionsniper.compass;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompassBar {

    List<Character> compass = new ArrayList<>();
    HashMap<String,Integer> compassPoints = new HashMap<>();

    public void Compass(Player player, BossBar compassBar) {

        float bearing = player.getLocation().getYaw() + 180;
        int screenWidth = 102;
        int compassScreenCoverage = 1;
        int fov = 95;
        int length = screenWidth * 360/fov;

        for (int i = 0; i < length; i++) {
            compass.add('-');
        }

        compassPoints.put("North",0);
        compassPoints.put("North East",45);
        compassPoints.put("East",90);
        compassPoints.put("South East",135);
        compassPoints.put("South",180);
        compassPoints.put("South West",225);
        compassPoints.put("West",270);
        compassPoints.put("North West",315);


        for (String compassPoint : compassPoints.keySet()) {

            String[] splitCompassPoint = compassPoint.split(" ",3);

            Integer compassPointBearing = compassPoints.get(compassPoint);
            int placement =  (compassPointBearing * length)/360;

            for (String ignored : splitCompassPoint) {
                compass.remove(placement);
            }
            for (int i = splitCompassPoint.length - 1; i >= 0;i--) {
                compass.add(placement,splitCompassPoint[i].charAt(0));
            }

        }

        String stringCompass = "";

        for (int i = 0; i < 3; i++) {
            for (Character character: compass) {
                stringCompass += String.valueOf(character);
            }
        }

        String compassSection = stringCompass.substring((int) (length + ((bearing - (screenWidth * compassScreenCoverage)/2) * length)/360), (int) (length + ((bearing + (screenWidth * compassScreenCoverage)/2) * length)/360));

        System.out.println(compassSection);
        compassBar.setTitle(compassSection);
    }
}
