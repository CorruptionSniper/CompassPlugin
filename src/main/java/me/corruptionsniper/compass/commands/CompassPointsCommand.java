package me.corruptionsniper.compass.commands;

import me.corruptionsniper.compass.compassPoints.CompassPoint;
import me.corruptionsniper.compass.compassPoints.PluginPlayerCompassPoints;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CompassPointsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PluginPlayerCompassPoints pluginPlayerCompassPoints = new PluginPlayerCompassPoints();

            if (args.length == 0) {
                List<CompassPoint> compassPoints = pluginPlayerCompassPoints.get(player);
                StringBuilder compassPointsMessageList = new StringBuilder();
                for (CompassPoint compassPoint: compassPoints) {
                    compassPointsMessageList.append("\n").append(compassPoint.getLabel()).append(", ").append(compassPoint.getBearing());
                }
                player.sendMessage("Compass Points:" + compassPointsMessageList);

            } else if (args[0].equals("add")) {
                StringBuilder argsToString = new StringBuilder();
                for (String arg : args) {
                    argsToString.append(arg).append(" ");
                }
                String[] filteredMessage = argsToString.substring(4).split(",",2);
                if (filteredMessage.length == 2) {
                    String compassPointLabel = filteredMessage[0].trim();
                    System.out.println("label " + compassPointLabel + "|bearing " + filteredMessage[1]);
                    try {
                        Float compassPointBearing = Float.parseFloat(filteredMessage[1].trim());
                        CompassPoint compassPoint = new CompassPoint(compassPointLabel, compassPointBearing);
                        pluginPlayerCompassPoints.putCompassPoint(player, compassPoint);
                        player.sendMessage("Compass Point '" + compassPointLabel + "' was added to your compass.");
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Invalid Syntax: The bearing must be a number.");
                    } catch (NullPointerException e) {
                        player.sendMessage(ChatColor.RED + "Invalid Syntax: Bearing not provided.");
                    }
                } else {player.sendMessage(ChatColor.RED + "Invalid format: The format must be in the form '/compassPoints add <label>,<bearing>'");}

            } else if (args[0].equals("remove")) {
                StringBuilder argsToString = new StringBuilder();
                for (String arg : args) {
                    argsToString.append(arg).append(" ");
                }
                String compassPointLabel = argsToString.substring(7).trim();
                System.out.println(compassPointLabel);
                boolean check = false;
                List<CompassPoint> compassPointList = pluginPlayerCompassPoints.get(player);
                CompassPoint compassPointToBeRemoved = new CompassPoint(null,null);
                for (CompassPoint compassPoint : compassPointList) {
                    if (compassPoint.getLabel().equals(compassPointLabel)) {
                        compassPointToBeRemoved = compassPoint;
                        check = true;
                        break;
                    }
                }
                if (check) {
                    pluginPlayerCompassPoints.remove(player,compassPointToBeRemoved);
                    player.sendMessage("Compass Point '" + compassPointLabel + "' was removed.");
                } else {player.sendMessage(ChatColor.RED + "Compass Point not found.");}

            } else if (args[0].equals("restoreDefaults")) {
                pluginPlayerCompassPoints.restoreDefaults(player);
                player.sendMessage("Compass Points restored to defaults.");

            } else {player.sendMessage(ChatColor.RED + "Command does not exist");}
        }

        return false;
    }
}
