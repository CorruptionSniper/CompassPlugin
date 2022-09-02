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

            } else if (args[0].equals("restoreDefaults")) {
                pluginPlayerSettings.restoreDefaults(player);
                player.sendMessage("Settings restored to default settings.");

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
            checkForError(player,settingError,"Invalid Setting:","to see a list of possible settings type /settings list.");
            checkForError(player,valueError,"Invalid values:","the only possible values are either 'true' or 'false'.");
            checkForError(player,formatError,"Invalid format:","to see the format type /settings format.");

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