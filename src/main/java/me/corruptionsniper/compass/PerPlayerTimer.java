package me.corruptionsniper.compass;

import me.corruptionsniper.compass.settings.PluginPlayerSettings;
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
    PluginPlayerSettings pluginPlayerSettings = new PluginPlayerSettings();

    //The method is run when a player joins the server.
    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        timer(player);
    }

    private int timerID;

    private void timer(Player player) {
        //Player BossBar(Compass).
        BossBar compassBar;
        //Creation of an instance of a BossBar(The Compass) for the player.
        compassBar = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);

        //Creation of an instance of a timer for the player which runs immediately every second (20 ticks).
        timerID = Bukkit.getScheduler().runTaskTimer(main, ()-> {
            //Checks that the player is in the server, and if not terminates the timer.
            if (!Bukkit.getOnlinePlayers().contains(player)) {Bukkit.getScheduler().cancelTask(timerID);}

            boolean compassSetting = pluginPlayerSettings.get(player).getCompass();
            boolean compassBarPlayerStatus = compassBar.getPlayers().contains(player);
            if (compassSetting) {
                //Runs the code which sets up the compass bar.
                compassBar.setTitle(new Compass().PlayerCompassSection(player));
                if (!compassBarPlayerStatus) {
                    compassBar.addPlayer(player);
                }
            } else if (compassBarPlayerStatus) {
                compassBar.removePlayer(player);
            }

        },0,20).getTaskId();
    }
}
