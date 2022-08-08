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
        int sectionSize = 90;
        int fov = 95;
        int length = sectionSize * 360/fov;

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

            char compassPointLabel = compassPoint.charAt(0);
            Integer compassPointBearing = compassPoints.get(compassPoint);

            int placement =  (compassPointBearing * length)/360;

            compass.remove(placement);
            compass.add(placement,compassPointLabel);
        }

        String stringCompass = "";

        for (int i = 0; i < 3; i++) {
            for (Character character: compass) {
                stringCompass += String.valueOf(character);
            }
        }

        String compassSection = stringCompass.substring((int) (length + ((bearing - sectionSize/2) * length)/360), (int) (length + ((bearing + sectionSize/2) * length)/360));

        System.out.println(compassSection);
        compassBar.setTitle(compassSection);
    }
}
