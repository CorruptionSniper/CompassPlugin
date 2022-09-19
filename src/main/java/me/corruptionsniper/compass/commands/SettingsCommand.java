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
                String guiScale = "guiScale: " + settings.getGuiScale() + "\n";
                String fov = "fov: " + settings.getFov() + "\n";
                String screenCoverage = "screenCoverage: " + settings.getScreenCoverage() + "\n";
                String resolution = "resolution: " + settings.getWidth() + "x" + settings.getHeight() + "\n";
                player.sendMessage("Settings:\n" + compass + guiScale + fov + screenCoverage + resolution);

            } else if (args[0].equalsIgnoreCase("restoreDefaults")) {
                pluginPlayerSettings.restoreDefaults(player);
                player.sendMessage("Settings restored to default settings.");

            } else if (args[0].equalsIgnoreCase("format")) {
                player.sendMessage(" Command format: '/settings <argument 1> <argument 2> ... <argument 3>'\nArgument format: <Setting>:<Value>\nAvailable settings: compassToggle, guiScale, fov, screenCoverage, dimensions\ntype '/settings info' for more detailed information for each setting");
            } else if (args[0].equalsIgnoreCase("info")) {
                StringBuilder informationMessage = new StringBuilder();
                String compassToggleInformation = "\n- CompassToggle\n  value: boolean (eg. true, false).\n  Toggles on/off the compass.";
                String guiScaleInformation = "\n- GuiScale\n  value: integer number\n  Resizes the compass to match the GUI scale of the player.";
                String fovInformation = "\n- FOV\n  value: integer number between 30 and 110\n  Changes the scaling of compass points to match the fov of the player.";
                String screenCoverageInformation = "\n- ScreenCoverage\n  value: number between 0 and 1 (eg. 1, 0.8, 0.9)\n  Adjusts the amount of the screen covered by the compass.";
                String resolutionInformation = "\n- Resolution\n  value: <width>x<height> (eg. 1920x1080, 1024x768)\n  Resizes the compass and scaling of compass points to match the screen of the player.\n  Warning: ";
                String tip = "\nto specify which setting to look at type '/info <setting>'";
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
                } else {informationMessage.append(compassToggleInformation).append(guiScaleInformation).append(fovInformation).append(screenCoverageInformation).append(resolutionInformation).append(tip);}
                player.sendMessage("Setting information:" + informationMessage);

            } else for (String arg : args) {
                String[] splitArg = arg.toLowerCase(Locale.ROOT).split(":", 2);

                if (splitArg.length == 2) {
                    switch (splitArg[0]) {
                        case "compasstoggle":
                            if (splitArg[1].equals("true")) {
                                settings.setCompass(true);
                            } else if (splitArg[1].equals("false")) {
                                settings.setCompass(false);
                            } else {
                                valueError.add(splitArg[1]);
                            }
                            break;
                        case "guiscale":
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
                        case "screencoverage":
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