package me.corruptionsniper.compass.commands;

import me.corruptionsniper.compass.CommandUtil;
import me.corruptionsniper.compass.compassPoints.CompassPoint;
import me.corruptionsniper.compass.compassPoints.PluginPlayerCompassPoints;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CompassPointsCommand implements CommandExecutor {
    PluginPlayerCompassPoints pluginPlayerCompassPoints = new PluginPlayerCompassPoints();
    CommandUtil commandUtil = new CommandUtil();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String l, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            ChatColor titleColour = ChatColor.LIGHT_PURPLE;
            ChatColor headingColour = ChatColor.YELLOW;
            ChatColor subHeadingColour = ChatColor.AQUA;

            if (args.length == 0) {
                List<CompassPoint> compassPoints = pluginPlayerCompassPoints.get(player);
                StringBuilder compassPointsMessageList = new StringBuilder();
                for (CompassPoint compassPoint: compassPoints) {
                    String properties = "";
                    switch (compassPoint.getType()) {
                        case "direction":
                            properties = commandUtil.setColour("bearing: ",subHeadingColour) + compassPoint.getBearing().toString();
                            break;
                        case "coordinate":
                            properties =  commandUtil.setColour("x: ",subHeadingColour) + compassPoint.getXCoordinate() + commandUtil.setColour(", z: ",subHeadingColour) + compassPoint.getZCoordinate();
                            break;
                    }
                    compassPointsMessageList.append("\n").append(compassPoint.getColour()).append(compassPoint.getLabel()).append(ChatColor.WHITE).append(" - ").append(properties);
                }
                player.sendMessage(commandUtil.setColour("Compass Points:", titleColour) + compassPointsMessageList);
            } else switch (args[0].toLowerCase(Locale.ROOT)) {
                case "restoredefaults":
                    pluginPlayerCompassPoints.put(player,pluginPlayerCompassPoints.defaultCompassPoints());
                    player.sendMessage(ChatColor.GREEN + "Compass points have been restored to defaults.");
                    break;
                case "format":
                    player.sendMessage(commandUtil.setColour("Compass Points Add Format: ", headingColour) + "'/compasspoints add <compasspoint 1>; <compasspoint 2>; ... <compasspoint n>'\n" + commandUtil.setColour("Argument format:", headingColour) + "\n<name>,<type>,<properties>\n\n" + commandUtil.setColour("Properties format:", headingColour) + commandUtil.setColour("\nif <type> is equal to direction: ",subHeadingColour) + "bearing, colour(optional)" +  commandUtil.setColour("\nif <type> is equal to coordinate: ",subHeadingColour) +"x coordinate, z coordinate, colour(optional)");
                    break;
                case "add":
                    List<String> typeErrorList = new ArrayList<>();
                    List<String> numberFormatErrorList = new ArrayList<>();
                    List<String> formatErrorList = new ArrayList<>();

                    List<String> compassPointsAdded = new ArrayList<>();
                    String[] compassPointsToAdd = commandUtil.join(Arrays.asList(args), " ").substring(4).split(";");

                    for (String compassPointToAdd : compassPointsToAdd) {
                        String[] compassPointProperties = compassPointToAdd.split(",");
                        String type = compassPointProperties[1].trim().toLowerCase(Locale.ROOT);
                        String label = compassPointProperties[0].trim();
                        Float degrees = null;
                        Float xCoordinate = null;
                        Float zCoordinate = null;
                        String colour = compassPointProperties[compassPointProperties.length - 1];
                        if (label.isEmpty()) {continue;}
                        try {
                            switch (type) {
                                case "direction":
                                    degrees = (float) Math.floorMod(Integer.parseInt(compassPointProperties[2].trim()), 360);
                                    break;
                                case "coordinate":
                                    xCoordinate = (float) Integer.parseInt(compassPointProperties[2].trim());
                                    zCoordinate = (float) Integer.parseInt(compassPointProperties[3].trim());
                                    break;
                                default:
                                    typeErrorList.add(label);
                                    continue;
                            }
                        } catch (NumberFormatException e) {
                            numberFormatErrorList.add(label);
                            continue;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            formatErrorList.add(label);
                            continue;
                        }
                        CompassPoint compassPoint = new CompassPoint(type,label,degrees,xCoordinate,zCoordinate,null);
                        compassPoint.setColour(colour);

                        pluginPlayerCompassPoints.putCompassPoint(player, compassPoint);
                        compassPointsAdded.add(label);
                    }
                    commandUtil.listingMessage(player, ChatColor.RED , ChatColor.RED,"Invalid format for:", formatErrorList,"type '/compassPoints format' to view the correct format.");
                    commandUtil.listingMessage(player, ChatColor.RED , ChatColor.RED,"Invalid number format for:", numberFormatErrorList,"the number inputted was invalid for these compass points.");
                    commandUtil.listingMessage(player, ChatColor.RED , ChatColor.RED,"Invalid compass type for:", typeErrorList,"the only available compass types are 'direction' and 'coordinate'");

                    commandUtil.listingMessage(player, ChatColor.GREEN, ChatColor.AQUA, "Compass points:", compassPointsAdded, "have been added to your compass");
                    break;
                case "remove":
                    List<String> compassPointNotFoundError = new ArrayList<>();
                    List<String> compassPointsRemoved = new ArrayList<>();

                    String[] compassPointsToRemove = commandUtil.join(Arrays.asList(args), " ").substring(7).split(";");
                    for (String compassPointToRemove : compassPointsToRemove) {
                        String label = compassPointToRemove.trim();
                        List<CompassPoint> compassPointList = pluginPlayerCompassPoints.get(player);
                        CompassPoint compassPointToBeRemoved = null;
                        for (CompassPoint compassPoint: compassPointList) {
                            if (compassPoint.getLabel().equals(label)) {
                                compassPointToBeRemoved = compassPoint;
                                break;
                            }
                        }

                        if (compassPointToBeRemoved != null) {
                            pluginPlayerCompassPoints.remove(player,compassPointToBeRemoved);
                            compassPointsRemoved.add(label);
                        } else {
                            compassPointNotFoundError.add(label);}
                    }
                    commandUtil.listingMessage(player, ChatColor.RED , ChatColor.RED, "Compass points have not been found:",compassPointNotFoundError,"make sure you have types these in correctly.");
                    commandUtil.listingMessage(player, ChatColor.GREEN, ChatColor.AQUA, "Compass points:",compassPointsRemoved,"have been successfully removed.");
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "The command does not exist");
            }
        }

        return false;
    }
}
