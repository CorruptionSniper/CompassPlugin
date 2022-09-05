package me.corruptionsniper.compass.commands;

import me.corruptionsniper.compass.settings.PluginPlayerSettings;
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PluginPlayerSettings pluginPlayerSettings = new PluginPlayerSettings();

            List<String> settingError = new ArrayList<>();
            List<String> valueError = new ArrayList<>();
            List<String> formatError = new ArrayList<>();

            Settings settings = pluginPlayerSettings.get(player);

            if (args.length == 0) {
                String compass = "Compass:";
                if (settings.getCompass()) {compass += "true\n";} else {compass += "false\n";}
                String guiScale = "GUI Scale: " + settings.getGuiScale() + "\n";
                String fov = "FOV: " + settings.getFov() + "\n";
                String screenCoverage = "Compass Screen Coverage: " + settings.getScreenCoverage() + "\n";
                String resolution = "Resolution: " + settings.getWidth() + "x" + settings.getHeight() + "\n";
                player.sendMessage("Settings:\n" + compass + guiScale + fov + screenCoverage + resolution);

            } else if (args[0].equalsIgnoreCase("restoreDefaults")) {
                pluginPlayerSettings.restoreDefaults(player);
                player.sendMessage("Settings restored to default settings.");

            } else if (args[0].equalsIgnoreCase("format")) {
                player.sendMessage("Argument format: <Setting>:<Boolean Value>\nSettings: compass, guiScale,");
            } else for (String arg : args) {
                String[] splitArg = arg.toLowerCase(Locale.ROOT).split(":", 2);

                if (splitArg.length == 2) {
                    switch (splitArg[0]) {
                        case "compass":
                        case "compassToggle":
                            if (splitArg[1].equals("true")) {
                                settings.setCompass(true);
                            } else if (splitArg[1].equals("false")) {
                                settings.setCompass(false);
                            } else {
                                valueError.add(splitArg[1]);
                            }
                            break;
                        case "guiScale":
                        case "gui":
                            try {
                                int intValueOfArg = Integer.parseInt(splitArg[1]);
                                if (intValueOfArg < 1) {
                                    valueError.add(splitArg[1]);
                                } else {settings.setGuiScale(intValueOfArg);}
                            } catch (NumberFormatException e) {
                                valueError.add(splitArg[1]);
                            }
                            break;
                        case "fov":
                            try {
                                int intValueOfArg = Integer.parseInt(splitArg[1]);
                                if (intValueOfArg < 30 | intValueOfArg > 110) {
                                    valueError.add(splitArg[1]);
                                } else {settings.setFov(intValueOfArg);}
                            } catch (NumberFormatException e) {
                                valueError.add(splitArg[1]);
                            }
                            break;
                        case "screenCoverage":
                        case "compassWidth":
                            try {
                                float floatValueOfArg = Float.parseFloat(splitArg[1]);
                                if (floatValueOfArg < 0 | floatValueOfArg > 1) {
                                    valueError.add(splitArg[1]);
                                } else {settings.setScreenCoverage(floatValueOfArg);}
                            } catch (NumberFormatException e) {
                                valueError.add(splitArg[1]);
                            }
                            break;
                        case "dimensions":
                        case "resolution":
                            String[] dimensions = splitArg[1].split("x",2);
                            if (dimensions.length != 2) {
                                valueError.add(splitArg[1]);
                            } else {
                                try{
                                    int height = Integer.parseInt(dimensions[1]);
                                    int width = Integer.parseInt(dimensions[0]);
                                    if (height < 0 | width < 0) {
                                        valueError.add(splitArg[1]);
                                    } else {settings.setHeight(height); settings.setWidth(width);}
                                } catch (NumberFormatException e) {
                                    valueError.add(splitArg[1]);
                                }
                            }
                            break;
                        default:
                            settingError.add(splitArg[0]);
                    }
                } else {formatError.add(arg);}
            }
            pluginPlayerSettings.put(player, settings);

            checkForError(player, settingError, "Invalid Settings:", "to see a list of possible settings type /settings format.");
            checkForError(player, valueError, "Invalid values:", "to see a list of values allowed for each setting type /settings format.");
            checkForError(player, formatError, "Invalid format:", "to see the format type /settings format.");
        }
        return false;
    }

    private void checkForError(Player player, List<String> errorList, String errorType, String suffixMessage) {
        if (!errorList.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder(errorType);
            for (String error : errorList) {
                errorMessage.append(" '").append(error).append("',");
            }
            errorMessage.append("\n").append(suffixMessage);
            player.sendMessage(ChatColor.RED + errorMessage.toString());
        }
    }
}