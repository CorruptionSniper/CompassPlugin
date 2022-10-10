package me.corruptionsniper.compass.commands;

import me.corruptionsniper.compass.CommandUtil;
import me.corruptionsniper.compass.compassPoints.CompassPoint;
import me.corruptionsniper.compass.compassPoints.PlayerCompassPoints;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CompassPointsCommand implements CommandExecutor {
    PlayerCompassPoints playerCompassPoints = new PlayerCompassPoints();
    CommandUtil commandUtil = new CommandUtil();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String l, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            ChatColor titleColour = ChatColor.LIGHT_PURPLE;
            ChatColor headingColour = ChatColor.YELLOW;
            ChatColor subHeadingColour = ChatColor.AQUA;

            if (args.length == 0) {
                SortedSet<CompassPoint> compassPoints = playerCompassPoints.get(player);
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
                    compassPointsMessageList.append("\n").append(compassPoint.getColour()).append(compassPoint.getLabel()).append(headingColour).append(" - ").append(properties);
                }
                player.sendMessage(commandUtil.setColour("Compass Points:", titleColour) + compassPointsMessageList);
            } else switch (args[0].toLowerCase(Locale.ROOT)) {
                case "restoredefaults":
                    playerCompassPoints.put(player, playerCompassPoints.defaultCompassPoints());
                    player.sendMessage(ChatColor.GREEN + "Compass points have been restored to defaults.");
                    break;
                case "format":
                    player.sendMessage(commandUtil.setColour("Compass Points Add Format: ", headingColour) + "'/compasspoints add <compasspoint 1>; <compasspoint 2>; ... <compasspoint n>'\n" + commandUtil.setColour("Argument format:", headingColour) + "\n<name>,<type>,<properties>\n\n" + commandUtil.setColour("Properties format:", headingColour) + commandUtil.setColour("\nif <type> is equal to 'direction': ", subHeadingColour) + "bearing, colour(optional)" + commandUtil.setColour("\nif <type> is equal to 'coordinate': ", subHeadingColour) + "x coordinate, z coordinate, colour(optional)");
                    break;
                case "add":
                    List<String> typeErrorList = new ArrayList<>();
                    List<String> numberFormatErrorList = new ArrayList<>();
                    List<String> formatErrorList = new ArrayList<>();

                    List<String> compassPointsAdded = new ArrayList<>();

                    String[] compassPointsToAdd = commandUtil.join(Arrays.asList(args), " ").substring(4).split(";");

                    for (String compassPointToAdd : compassPointsToAdd) {
                        String[] compassPointProperties = compassPointToAdd.split(",");
                        if (compassPointProperties.length < 2) {
                            continue;
                        }
                        String type = compassPointProperties[1].trim().toLowerCase(Locale.ROOT);
                        String label = compassPointProperties[0].trim();
                        Float degrees = null;
                        Float xCoordinate = null;
                        Float zCoordinate = null;
                        String colour = compassPointProperties[compassPointProperties.length - 1].trim().toLowerCase(Locale.ROOT);
                        if (label.isEmpty()) {
                            continue;
                        }
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
                        CompassPoint compassPoint = new CompassPoint(type, label, degrees, xCoordinate, zCoordinate, null);
                        compassPoint.setColour(colour);

                        playerCompassPoints.putCompassPoint(player, compassPoint);
                        compassPointsAdded.add(label);
                    }
                    commandUtil.listingMessage(player, ChatColor.RED, ChatColor.RED, "Invalid format for:", formatErrorList, "type '/compassPoints format' to view the correct format.");
                    commandUtil.listingMessage(player, ChatColor.RED, ChatColor.RED, "Invalid value for:", numberFormatErrorList, "the number inputted was invalid for these compass points.");
                    commandUtil.listingMessage(player, ChatColor.RED, ChatColor.RED, "Invalid type for:", typeErrorList, "the only available compass types are 'direction' and 'coordinate'");

                    commandUtil.listingMessage(player, ChatColor.GREEN, ChatColor.AQUA, "Compass points:", compassPointsAdded, "have been added to your compass");

                    break;
                case "remove":
                    List<String> compassPointsNotFoundError0 = new ArrayList<>();
                    List<String> compassPointsRemoved = new ArrayList<>();

                    String[] compassPointsToRemove = commandUtil.join(Arrays.asList(args), " ").substring(7).split(";");
                    for (String compassPointToRemove : compassPointsToRemove) {
                        String label = compassPointToRemove.trim();

                        if (playerCompassPoints.removeCompassPoint(player,label)) {
                            compassPointsRemoved.add(label);
                        } else {
                            compassPointsNotFoundError0.add(label);
                        }
                    }
                    commandUtil.listingMessage(player, ChatColor.RED , ChatColor.RED, "Compass points have not been found:",compassPointsNotFoundError0,"make sure you have types these in correctly, and that different compass points have been separated by a ';'.");
                    commandUtil.listingMessage(player, ChatColor.GREEN, ChatColor.AQUA, "Compass points:",compassPointsRemoved,"have been successfully removed.");
                    break;
                case "modify":
                    List<String> compassPointsNotFoundError1 = new ArrayList<>();
                    List<String> propertyError = new ArrayList<>();
                    List<String> valueError = new ArrayList<>();
                    List<String> formatError = new ArrayList<>();

                    List<String> compassPointsModified = new ArrayList<>();

                    String[] targets = commandUtil.join(Arrays.asList(args), " ").substring(7).split(";");
                    for (String target : targets) {
                        String[] targetSplit = target.split(",");
                        String targetLabel = targetSplit[0].trim();
                        CompassPoint compassPointToModify = playerCompassPoints.getCompassPoint(player,targetLabel);
                        if (compassPointToModify != null) {
                            playerCompassPoints.removeCompassPoint(player,compassPointToModify);
                            for (String property : targetSplit) {
                                String[] propertySplit = property.split(":",2);
                                if (propertySplit.length == 2) {
                                    String value = propertySplit[1].trim();
                                    try {
                                        switch (propertySplit[0].toLowerCase(Locale.ROOT)) {
                                            case "label":
                                                compassPointToModify.setLabel(value);
                                                break;
                                            case "type":
                                                switch (value.toLowerCase(Locale.ROOT)) {
                                                    case "direction":
                                                        compassPointToModify.setType("direction");
                                                        if (compassPointToModify.getBearing() == null) {
                                                            compassPointToModify.setBearing(player.getLocation().getYaw() + 180);
                                                        }
                                                        break;
                                                    case "coordinate":
                                                        compassPointToModify.setType("coordinate");
                                                        if (compassPointToModify.getXCoordinate() == null) {
                                                            compassPointToModify.setXCoordinate((float) player.getLocation().getX());
                                                        }
                                                        if (compassPointToModify.getZCoordinate() == null) {
                                                            compassPointToModify.setZCoordinate((float) player.getLocation().getZ());
                                                        }
                                                        break;
                                                    default:
                                                        valueError.add(targetLabel);
                                                }
                                                break;
                                            case "bearing":
                                                compassPointToModify.setBearing(Float.parseFloat(value));
                                                break;
                                            case "x-coordinate":
                                                compassPointToModify.setXCoordinate(Float.parseFloat(value));
                                                break;
                                            case "z-coordinate":
                                                compassPointToModify.setZCoordinate(Float.parseFloat(value));
                                                break;
                                            default:
                                                propertyError.add(targetLabel);
                                        }
                                    } catch (NumberFormatException numberFormatException) {
                                        valueError.add(targetLabel);
                                    }
                                }
                            }

                            playerCompassPoints.putCompassPoint(player,compassPointToModify);
                            compassPointsModified.add(targetLabel);
                        } else {
                            compassPointsNotFoundError1.add(targetLabel);
                        }
                    }

                    commandUtil.listingMessage(player,ChatColor.RED,ChatColor.RED,"Invalid format for:",formatError,"type '/compassPoints format' to view the correct format.");
                    commandUtil.listingMessage(player,ChatColor.RED,ChatColor.RED,"Invalid property for:",propertyError,"the property was invalid for these compass points.");
                    commandUtil.listingMessage(player,ChatColor.RED,ChatColor.RED,"Invalid value for:",valueError,"the number inputted was invalid for these compass points.");
                    commandUtil.listingMessage(player,ChatColor.RED,ChatColor.RED,"Compass Points:",compassPointsNotFoundError1,"have not been found.");

                    commandUtil.listingMessage(player,ChatColor.GREEN,ChatColor.AQUA,"Compass Points:",compassPointsModified,"have been successfully changed.");

                    break;
                default:
                    player.sendMessage(ChatColor.RED + "The command does not exist");
            }
        }
        return false;
    }
}
