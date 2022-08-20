package me.corruptionsniper.compass;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SettingsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PluginPlayerSettings pluginPlayerSettings = new PluginPlayerSettings();

            List<String> settingError = new ArrayList<>();
            List<String> valueError = new ArrayList<>();
            List<String> formatError = new ArrayList<>();

            if (args.length == 0) {
                Settings settings = pluginPlayerSettings.get(player);
                String compass = "compass:";
                if (settings.getCompass()) {compass += "true";} else {compass += "false";}
                player.sendMessage("Settings: " + compass);

            } else if (args[0].equals("list")) {
                player.sendMessage("Available Settings: 'compass'.");

            } else if (args[0].equals("format")) {
                player.sendMessage("Argument format: <Setting>:<Boolean Value>");

            } else for (String arg : args) {
                String[] splitArg = arg.split(":",2);

                if (splitArg.length == 2) {
                    String setting = null;
                    switch (splitArg[0]) {
                        case "compass":
                            setting = "compass";
                            break;
                    }

                    Boolean value = null;
                    switch (splitArg[1]) {
                        case "true":
                            value = true;
                            break;
                        case "false":
                            value = false;
                            break;
                    }

                    if (setting == null) {settingError.add(splitArg[0]);}
                    if (value == null) {valueError.add(splitArg[1]);}
                    if (setting != null && value != null) {
                        pluginPlayerSettings.changeSetting(player,setting,value);
                    }
                } else {formatError.add(arg);}
            }

            if (!settingError.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Invalid Setting:");
                for (String error : settingError) {
                    errorMessage.append(" '").append(error).append("',");
                }
                errorMessage.append("\nto see a list of possible settings type /settings list.");
                player.sendMessage(ChatColor.RED + errorMessage.toString());
            }
            if (!valueError.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Invalid values:");
                for (String error : valueError) {
                    errorMessage.append(" '").append(error).append("',");
                }
                errorMessage.append("\nthe only possible values are either 'true' or 'false'.");
                player.sendMessage(ChatColor.RED + errorMessage.toString());
            }
            if (!formatError.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Invalid format:");
                for (String error : formatError) {
                    errorMessage.append(" '").append(error).append("',");
                }
                errorMessage.append("\nto see the format type /settings format.");
                player.sendMessage(ChatColor.RED + errorMessage.toString());
            }

        }
        return false;
    }
}