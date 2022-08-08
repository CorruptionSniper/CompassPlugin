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

    private BossBar compassBar;
    private int compassBarUpdaterID;

    @EventHandler
    private void onJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();

        compassBar = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        compassBar.addPlayer(player);

        compassBarUpdaterID = Bukkit.getScheduler().runTaskTimer(main, ()-> {
            if (!Bukkit.getOnlinePlayers().contains(player)) {Bukkit.getScheduler().cancelTask(compassBarUpdaterID);}

            CompassBar comp = new CompassBar();
            comp.Compass(player, compassBar);

        },0,40).getTaskId();

    }


}
