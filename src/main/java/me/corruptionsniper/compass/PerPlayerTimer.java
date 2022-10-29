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

import java.util.HashMap;
import java.util.UUID;

public class PerPlayerTimer implements Listener {
    private final Main main;
    public PerPlayerTimer(Main main) {
        this.main = main;
    }
    PlayerSettings playerSettings = new PlayerSettings();

    private static HashMap<UUID, Integer> cleanupMap = new HashMap<>();

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        //Starting of an instance of a timer for the player.
        timer(event.getPlayer());
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        //Deletion of the player's timer instance.
        Bukkit.getScheduler().cancelTask(cleanupMap.get(player.getUniqueId()));
        //Removing of the player from the hashmap.
        cleanupMap.remove(player.getUniqueId());
    }

    int period = 20;

    private void timer(Player player) {
        //Creation of an instance of a BossBar(The Compass) for the player.
        BossBar compassBar = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);

        //Creation of an instance of a timer for the player,
        //Whilst its task ID is stored into a hashmap with the player's UUID.
        //(So that when the player leaves the server, the hashmap can be accessed to disable the timer).
        cleanupMap.put(player.getUniqueId(), Bukkit.getScheduler().runTaskTimer(main, ()-> {
            boolean compassSetting = playerSettings.get(player).getCompass();
            boolean compassBarContainsPlayer = compassBar.getPlayers().contains(player);
            System.out.println("running");
            if (compassSetting) {
                if (!compassBarContainsPlayer) {
                    compassBar.addPlayer(player);
                }
                compassBar.setTitle(new Compass().compassGenerator(player));
            } else if (compassBarContainsPlayer) {
                compassBar.removePlayer(player);
            }
        },0,period).getTaskId());
    }
}
