package me.corruptionsniper.compass;

import me.corruptionsniper.compass.settings.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class PerPlayerTimer implements Listener {
    private static final int PERIOD = 20;
    private static final PlayerSettings playerSettings = new PlayerSettings();

    private final Main main;

    public PerPlayerTimer(Main main) {
        this.main = main;
    }


    private static final HashMap<UUID, Integer> timerMap = new HashMap<>();

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        saveTimer(player);
    }

    private void saveTimer(Player player) {
        timerMap.put(player.getUniqueId(), startTimer(player).getTaskId());
    }

    private BukkitTask startTimer(Player player) {
        return Bukkit.getScheduler().runTaskTimer(main, setupTimer(player),0, PERIOD);
    }

    private Runnable setupTimer(Player player) {
        //Creation of an instance of a BossBar(The Compass) for the player.
        BossBar compass = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        Compass playerCompass = new Compass(player);
        return ()-> timer(player, compass, playerCompass);
    }

    private void timer(Player player, BossBar compass, Compass playerCompass) {
        boolean isCompassOn = playerSettings.get(player).getCompass();
        boolean compassBarContainsPlayer = compass.getPlayers().contains(player);
        if (isCompassOn) {
            if (!compassBarContainsPlayer) {
                compass.addPlayer(player);
            }
            compass.setTitle(playerCompass.compassGenerator());
        } else if (compassBarContainsPlayer) {
            compass.removePlayer(player);
        }
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        stopTimer(player);
    }

    private void stopTimer(Player player) {
        //Deletion of the player's timer instance.
        Bukkit.getScheduler().cancelTask(timerMap.get(player.getUniqueId()));
        //Removing of the player from the hashmap.
        timerMap.remove(player.getUniqueId());
    }
}
