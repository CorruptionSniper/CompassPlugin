package me.corruptionsniper.compass.commands;

import me.corruptionsniper.compass.CommandUtil;
import me.corruptionsniper.compass.settings.PlayerSettings;
import me.corruptionsniper.compass.settings.Settings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SettingsCommand implements CommandExecutor {

    PlayerSettings playerSettings = new PlayerSettings();
    CommandUtil commandUtil = new CommandUtil();

    private final ChatColor titleColour = ChatColor.LIGHT_PURPLE;
    private final ChatColor headingColour = ChatColor.YELLOW;
    private final ChatColor subHeadingColour = ChatColor.AQUA;

    List<String> changedSettings;

    List<String> settingError = new ArrayList<>();
    List<String> valueError = new ArrayList<>();
    List<String> formatError = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {
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
                case "info":
                    info(player, args);
                    break;
                default:
                    settings(player, args);
                    break;
            }
        }
        return false;
    }

    private void standard(Player player) {
        Settings settings = playerSettings.get(player.getUniqueId());
        String compass = commandUtil.setColour("Compass: ", headingColour);
        if (settings.getCompass()) {
            compass += "true\n";
        } else {
            compass += "false\n";
        }
        String guiScale = commandUtil.setColour("Gui Scale: ", headingColour) + settings.getGuiScale() + "\n";
        String fov = commandUtil.setColour("FOV: ", headingColour) + settings.getFov() + "\n";
        String screenCoverage = commandUtil.setColour("Compass Screen Coverage: ", headingColour) + settings.getScreenCoverage() + "\n";
        String resolution = commandUtil.setColour("Resolution: ", headingColour) + settings.getWidth() + "x" + settings.getHeight() + "\n";
        player.sendMessage(commandUtil.setColour("Settings:\n", titleColour) + compass + guiScale + fov + screenCoverage + resolution);
    }

    private void restoreDefaults(Player player) {
        playerSettings.restoreDefaults(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "Settings have been restored to the default settings.");
    }

    private void format(Player player) {
        player.sendMessage(commandUtil.setColour("Command Format: ", headingColour) + "'/settings <argument 1> <argument 2> ... <argument n>'\n" + commandUtil.setColour("Argument format:", headingColour) + "<Setting>:<Value>\n" + commandUtil.setColour("Available settings:", headingColour) + "compassToggle, guiScale, fov, screenCoverage, dimensions\n" + ChatColor.GRAY + "type '/settings info' for more detailed information for each setting");
    }

    private void info(Player player, String[] args) {
        StringBuilder informationMessage = new StringBuilder();
        String compassToggleInformation = commandUtil.setColour("\n- CompassToggle ", headingColour) + commandUtil.setColour("\n  Value: ", subHeadingColour) + "boolean (eg. true, false).\n  Toggles on/off the compass.";
        String guiScaleInformation = commandUtil.setColour("\n- GuiScale", headingColour) + commandUtil.setColour("\n  Value: ", subHeadingColour) + "integer number\n  Resizes the compass to match the GUI scale of the player.";
        String fovInformation = commandUtil.setColour("\n- FOV", headingColour) + commandUtil.setColour("\n  Value: ", subHeadingColour) + "integer number between 30 and 110\n  Changes the scaling of compass points to match the fov of the player.";
        String screenCoverageInformation = commandUtil.setColour("\n- ScreenCoverage", headingColour) + commandUtil.setColour("\n  Value: ", subHeadingColour) + "number between 0 and 1 (eg. 1, 0.8, 0.9)\n  Adjusts the amount of the screen covered by the compass.";
        String resolutionInformation = commandUtil.setColour("\n- Resolution", headingColour) + commandUtil.setColour("\n  Value: ", subHeadingColour) + "<width>x<height> (eg. 1920x1080, 1024x768)\n  Resizes the compass and scaling of compass points to match the screen of the player.\n" + commandUtil.setColour("  Warning: ", ChatColor.RED) + "The only supported resolutions are those with aspect ratio 16:9 and 4:3";
        String tip = ChatColor.GRAY + "\nto specify which setting to look at type '/settings info <setting>'";
        if (args.length != 1) {
            for (String arg : args) {
                switch (arg) {
                    case "compassToggle":
                        informationMessage.append(compassToggleInformation);
                        break;
                    case "guiScale":
                        informationMessage.append(guiScaleInformation);
                        break;
                    case "screenCoverage:":
                        informationMessage.append(screenCoverageInformation);
                        break;
                    case "resolution":
                        informationMessage.append(resolutionInformation);
                        break;
                }
            }
        } else {
            informationMessage.append(compassToggleInformation).append(guiScaleInformation).append(fovInformation).append(screenCoverageInformation).append(resolutionInformation).append(tip);
        }
        player.sendMessage(commandUtil.setColour("Setting information:\n", titleColour) + informationMessage);
    }

    private void settings(Player player, String[] args) {
        changedSettings = new ArrayList<>();
        Settings settings = playerSettings.get(player.getUniqueId());
        for (String arg : args) {
            String[] splitArg = arg.toLowerCase(Locale.ROOT).split(":", 2);

            if (splitArg.length == 2) {
                switch (splitArg[0]) {
                    case "compasstoggle":
                        switch (splitArg[1].toLowerCase(Locale.ROOT)) {
                            case "true":
                                settings.setCompass(true);
                                break;
                            case "false":
                                settings.setCompass(false);
                                break;
                            default:
                                valueError.add(splitArg[1]);
                                continue;
                        }
                        changedSettings.add("compassToggle");
                        break;
                    case "guiscale":
                        try {
                            int intValueOfArg = Integer.parseInt(splitArg[1]);
                            if (intValueOfArg < 1) {
                                valueError.add(splitArg[1]);
                            } else {
                                settings.setGuiScale(intValueOfArg);
                            }
                        } catch (NumberFormatException e) {
                            valueError.add(splitArg[1]);
                            continue;
                        }
                        changedSettings.add("guiScale");
                        break;
                    case "fov":
                        try {
                            int intValueOfArg = Integer.parseInt(splitArg[1]);
                            if (intValueOfArg < 30 | intValueOfArg > 110) {
                                valueError.add(splitArg[1]);
                                continue;
                            } else {
                                settings.setFov(intValueOfArg);
                            }
                        } catch (NumberFormatException e) {
                            valueError.add(splitArg[1]);
                            continue;
                        }
                        changedSettings.add("fov");
                        break;
                    case "screencoverage":
                        try {
                            float floatValueOfArg = Float.parseFloat(splitArg[1]);
                            if (floatValueOfArg < 0 | floatValueOfArg > 1) {
                                valueError.add(splitArg[1]);
                                continue;
                            } else {
                                settings.setScreenCoverage(floatValueOfArg);
                            }
                        } catch (NumberFormatException e) {
                            valueError.add(splitArg[1]);
                            continue;
                        }
                        changedSettings.add("screenCoverage");
                        break;
                    case "dimensions":
                        String[] dimensions = splitArg[1].split("x", 2);
                        if (dimensions.length != 2) {
                            valueError.add(splitArg[1]);
                        } else {
                            try {
                                int height = Integer.parseInt(dimensions[1]);
                                int width = Integer.parseInt(dimensions[0]);
                                if (height < 0 | width < 0) {
                                    valueError.add(splitArg[1]);
                                    continue;
                                } else {
                                    settings.setHeight(height);
                                    settings.setWidth(width);
                                }
                            } catch (NumberFormatException e) {
                                valueError.add(splitArg[1]);
                                continue;
                            }
                        }
                        changedSettings.add("dimensions");
                        break;
                    default:
                        settingError.add(splitArg[0]);
                        break;
                }

            } else {
                formatError.add(arg);
            }
            playerSettings.put(player.getUniqueId(), settings);
        }
        commandUtil.listingMessage(player, ChatColor.RED, ChatColor.RED, "Invalid Settings:", settingError, "to see a list of possible settings type /settings format.");
        commandUtil.listingMessage(player, ChatColor.RED, ChatColor.RED, "Invalid values:", valueError, "to see a list of values allowed for each setting type /settings format.");
        commandUtil.listingMessage(player, ChatColor.RED, ChatColor.RED, "Invalid format:", formatError, "to see the format type /settings format.");

        commandUtil.listingMessage(player, ChatColor.GREEN, ChatColor.AQUA, "Settings:", changedSettings, "have been successfully changed.");
    }
}