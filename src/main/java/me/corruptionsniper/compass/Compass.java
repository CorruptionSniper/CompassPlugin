package me.corruptionsniper.compass;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

public class Compass implements Listener {
    private Main main;
    public Compass(Main main) {
        this.main = main;
    }

    private BossBar compassBar;


    @EventHandler
    private void onJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();

        compassBar = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        compassBar.addPlayer(player);

        BukkitTask compassBarUpdater = Bukkit.getScheduler().runTaskTimer(main, ()-> {
            Integer Yaw = (int) player.getLocation().getYaw();

            String direction;
            if (-157.5 < Yaw & Yaw < -112.5) {direction = "NE";}
            else if (-112.5 < Yaw & Yaw < -67.5) {direction = "E";}
            else if (-67.5 < Yaw & Yaw < -22.5) {direction = "SE";}
            else if (-22.5 < Yaw & Yaw < 22.5) {direction = "S";}
            else if (22.5 < Yaw & Yaw < 67.5) {direction = "SW";}
            else if (67.5 < Yaw & Yaw < 112.5) {direction = "W";}
            else if (112.5 < Yaw & Yaw < 157.5) {direction = "NW";}
            else {direction = "N";}

            player.sendMessage(direction);
            compassBar.setTitle(direction);
        },40,40);
    }
}
