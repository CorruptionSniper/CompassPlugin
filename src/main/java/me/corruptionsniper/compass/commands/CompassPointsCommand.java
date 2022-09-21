package me.corruptionsniper.compass.commands;

import me.corruptionsniper.compass.compassPoints.CompassPoint;
import me.corruptionsniper.compass.compassPoints.PluginPlayerCompassPoints;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

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
                    compassPointsMessageList.append("\n").append(compassPoint.getLabel()).append(", ").append(compassPoint.getColour()).append(compassPoint.getBearing());
                }
                player.sendMessage("Compass Points:" + compassPointsMessageList);

            } else if (args[0].equalsIgnoreCase("add")) {
                if (args.length > 2) {
                    String[] filteredMessage = argsToString(args).substring(args[0].length() + args[1].length() + 2).split(",",4);
                    String compassPointLabel = filteredMessage[0].trim();
                    if (args[1].equalsIgnoreCase("direction")) {
                        if (filteredMessage.length == 2 | filteredMessage.length == 3) {
                            try {
                                float compassPointBearing = Math.floorMod((int) Float.parseFloat(filteredMessage[1].trim()), 360);
                                CompassPoint compassPoint = new CompassPoint("direction", compassPointLabel, compassPointBearing, null, null, null);
                                if (filteredMessage.length == 2) {compassPoint.setColour(null);}
                                else {compassPoint.setColour(filteredMessage[2].toLowerCase(Locale.ROOT).trim());}
                                pluginPlayerCompassPoints.putCompassPoint(player, compassPoint);
                                player.sendMessage("Compass Point '" + compassPointLabel + "' was added to your compass.");
                            } catch (NumberFormatException e) {
                                player.sendMessage(ChatColor.RED + "Invalid Syntax: The bearing must be a number.");
                            } catch (NullPointerException e) {
                                player.sendMessage(ChatColor.RED + "Invalid Syntax: Compass point bearing not provided.");
                            }
                        } else {player.sendMessage(ChatColor.RED + "Invalid format: The format must be in the form '/compassPoints add direction <label>,<bearing>,'<colour>''.");}
                    }  else if (args[1].equalsIgnoreCase("coordinate")) {
                        if (filteredMessage.length == 3 | filteredMessage.length == 4) {
                            try {
                                float xCoordinate = Float.parseFloat(filteredMessage[1].trim());
                                float zCoordinate = Float.parseFloat(filteredMessage[2].trim());
                                CompassPoint compassPoint = new CompassPoint("coordinate", compassPointLabel, null, xCoordinate, zCoordinate, null);
                                if (filteredMessage.length == 3) {compassPoint.setColour(null);}
                                else {compassPoint.setColour(filteredMessage[3].toLowerCase(Locale.ROOT).trim());}
                                pluginPlayerCompassPoints.putCompassPoint(player, compassPoint);
                                player.sendMessage("Compass Point '" + compassPointLabel + "' was added to your compass.");
                            } catch (NumberFormatException e) {
                                player.sendMessage(ChatColor.RED + "Invalid Syntax: The coordinates must be numbers.");
                            } catch (NullPointerException e) {
                                player.sendMessage(ChatColor.RED + "Invalid Syntax: The coordinates have not been provided.");
                            }
                        } else {player.sendMessage(ChatColor.RED + "Invalid format: The format must be in the form '/compassPoints add coordinates <label>,<x coordinate>,<z coordinate>,<colour>'.");}
                    }  else {player.sendMessage(ChatColor.RED + "Invalid syntax: Provided compass point type does not exist.");}
                } else {player.sendMessage(ChatColor.RED + "Invalid format: The format must be in the form '/compassPoints add direction <label>,<bearing>,<colour>' or '/compassPoints add coordinates <label>,<x coordinate>,<z coordinate>,<colour>'.");}

            } else if (args[0].equals("remove")) {
                String compassPointLabel = argsToString(args).substring(7).trim();
                System.out.println(compassPointLabel);
                boolean check = false;
                List<CompassPoint> compassPointList = pluginPlayerCompassPoints.get(player);
                CompassPoint compassPointToBeRemoved = new CompassPoint(null, null, null, null, null, null);
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
                pluginPlayerCompassPoints.put(player,pluginPlayerCompassPoints.defaultCompassPoints());
                player.sendMessage("Compass Points restored to defaults.");

            } else {player.sendMessage(ChatColor.RED + "Command does not exist");}
        }

        return false;
    }

    private String argsToString(String[] args) {
        StringBuilder argsToString = new StringBuilder();
        for (String arg : args) {
            argsToString.append(arg).append(" ");
        }
        return argsToString.toString();
    }
}
