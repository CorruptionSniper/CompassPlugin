package me.corruptionsniper.compass;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PerPlayerTimer implements Listener {
    private Main main;
    public PerPlayerTimer(Main main) {
        this.main = main;
    }

    //Player BossBar(Compass).
    private BossBar compassBar;
    //ID for the timer.
    private int timerID;

    //The method is run when a player joins the server.
    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        JsonFiles jsonFiles = new JsonFiles(main);
        if (jsonFiles.getPlayerSettings(player).getCompass()) {
            timer(player);
        }

    }

    private void timer(Player player) {
        //Creation of an instance of a BossBar(Compass) for the player.
        compassBar = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        compassBar.addPlayer(player);

        //Creation of an instance of a timer for the player which runs immediately every second (20 ticks).
        timerID = Bukkit.getScheduler().runTaskTimer(main, ()-> {
            //Checks that the player is in the server, and if not terminates the timer.
            if (!Bukkit.getOnlinePlayers().contains(player)) {Bukkit.getScheduler().cancelTask(timerID);}

            //Runs the code which sets up the compass bar.
            Compass compass = new Compass();
            compassBar.setTitle(compass.PlayerCompassSection(player));

        },0,20).getTaskId();
    }
}
