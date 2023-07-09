package me.corruptionsniper.compass.commands;

import me.corruptionsniper.compass.CommandUtil;
import me.corruptionsniper.compass.compassPoints.PlayerCompassPoints;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CompassPointsCommand implements CommandExecutor {
    private final PlayerCompassPoints playerCompassPoints = new PlayerCompassPoints();
    private final CommandUtil commandUtil = new CommandUtil();

    private final ChatColor titleColour = ChatColor.LIGHT_PURPLE;
    private final ChatColor headingColour = ChatColor.YELLOW;
    private final ChatColor subHeadingColour = ChatColor.AQUA;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /*if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                standard(player);
            } else switch (args[0].toLowerCase(Locale.ROOT)) {
                case "restoredefaults":
                    restoreDefaults(player);
                    break;
                case "format":
                    format(player);
                    break;
                case "add":
                    add(player,args);
                    break;
                case "remove":
                    remove(player, args);
                    break;
                case "modify":
                    modify(player, args);
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "The command does not exist");
            }
        }
        return false;
    }

    private void standard(Player player) {
        SortedSet<CompassPoint> compassPoints = playerCompassPoints.get(player.getUniqueId());
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
    }

    private void restoreDefaults(Player player) {
        playerCompassPoints.restoreDefaults(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "Compass points have been restored to defaults.");
    }

    private void format(Player player) {
        player.sendMessage(commandUtil.setColour("Compass Points Add Format: ", headingColour) + "'/compasspoints add <compasspoint 1>; <compasspoint 2>; ... <compasspoint n>'\n" + commandUtil.setColour("Argument format:", headingColour) + "\n<name>,<type>,<properties>\n\n" + commandUtil.setColour("Properties format:", headingColour) + commandUtil.setColour("\nif <type> is equal to 'direction': ", subHeadingColour) + "bearing, colour(optional)" + commandUtil.setColour("\nif <type> is equal to 'coordinate': ", subHeadingColour) + "x coordinate, z coordinate, colour(optional)");
    }

    private void add(Player player, String[] args) {
        List<String> typeError = new ArrayList<>();
        List<String> numberFormatError = new ArrayList<>();
        List<String> formatError = new ArrayList<>();

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
                        typeError.add(label);
                        continue;
                }
            } catch (NumberFormatException e) {
                numberFormatError.add(label);
                continue;
            } catch (ArrayIndexOutOfBoundsException e) {
                formatError.add(label);
                continue;
            }
            CompassPoint compassPoint = new CompassPoint(type, label, degrees, xCoordinate, zCoordinate, null);
            compassPoint.setColour(colour);

            //playerCompassPoints.putCompassPoint(player, compassPoint);
            compassPointsAdded.add(label);
        }
        commandUtil.listingMessage(player, ChatColor.RED, ChatColor.RED, "Invalid format for:", formatError, "type '/compassPoints format' to view the correct format.");
        commandUtil.listingMessage(player, ChatColor.RED, ChatColor.RED, "Invalid value for:", numberFormatError, "the number inputted was invalid for these compass points.");
        commandUtil.listingMessage(player, ChatColor.RED, ChatColor.RED, "Invalid type for:", typeError, "the only available compass types are 'direction' and 'coordinate'");

        commandUtil.listingMessage(player, ChatColor.GREEN, ChatColor.AQUA, "Compass points:", compassPointsAdded, "have been added to your compass");
    }

    private void remove(Player player, String[] args) {
        List<String> notFoundError = new ArrayList<>();
        List<String> cpRemoved = new ArrayList<>();

        String[] compassPointsToRemove = commandUtil.join(Arrays.asList(args), " ").substring(7).split(";");
        for (String compassPointToRemove : compassPointsToRemove) {
            String label = compassPointToRemove.trim();

            if (playerCompassPoints.removeCompassPoint(player,label)) {
                cpRemoved.add(label);
            } else {
                notFoundError.add(label);
            }
        }
        commandUtil.listingMessage(player, ChatColor.RED , ChatColor.RED, "Compass points have not been found:",notFoundError,"make sure you have types these in correctly, and that different compass points have been separated by a ';'.");
        commandUtil.listingMessage(player, ChatColor.GREEN, ChatColor.AQUA, "Compass points:",cpRemoved,"have been successfully removed.");
    }

    private void modify(Player player, String[] args) {
        List<String> notFoundError = new ArrayList<>();
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

                //playerCompassPoints.putCompassPoint(player,compassPointToModify);
                compassPointsModified.add(targetLabel);
            } else {
                notFoundError.add(targetLabel);
            }
        }

        commandUtil.listingMessage(player,ChatColor.RED,ChatColor.RED,"Invalid format for:",formatError,"type '/compassPoints format' to view the correct format.");
        commandUtil.listingMessage(player,ChatColor.RED,ChatColor.RED,"Invalid property for:",propertyError,"the property was invalid for these compass points.");
        commandUtil.listingMessage(player,ChatColor.RED,ChatColor.RED,"Invalid value for:",valueError,"the number inputted was invalid for these compass points.");
        commandUtil.listingMessage(player,ChatColor.RED,ChatColor.RED,"Compass Points:",notFoundError,"have not been found.");

        commandUtil.listingMessage(player,ChatColor.GREEN,ChatColor.AQUA,"Compass Points:",compassPointsModified,"have been successfully changed.");
    */return false;}
}
