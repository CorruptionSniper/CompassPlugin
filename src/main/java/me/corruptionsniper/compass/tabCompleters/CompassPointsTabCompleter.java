package me.corruptionsniper.compass.tabCompleters;

import me.corruptionsniper.compass.compassPoints.CompassPoint;
import me.corruptionsniper.compass.compassPoints.PluginPlayerCompassPoints;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CompassPointsTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            List<String> options = new ArrayList<>();

            if (args.length == 1) {
                options.add("add");
                options.add("remove");
            } else if (args[0].equalsIgnoreCase("add")) {
                String[] stringArgs = argsToString(args).split(",",4);
                List<String> colourList = Arrays.asList("black", "dark blue", "dark green", "dark aqua", "dark red", "dark purple", "gold", "gray", "dark gray", "blue", "green", "aqua", "red", "light purple", "yellow", "white");
                if (args.length == 2) {
                    options.add("direction");
                    options.add("coordinate");
                } else if (args[1].equalsIgnoreCase("direction")){
                    switch (stringArgs.length) {
                        case 2:
                            options.add(String.valueOf((int) player.getLocation().getYaw()));
                        case 3:
                            options = colourList;
                            break;
                    }
                } else if (args[1].equalsIgnoreCase("coordinate")) {
                    switch (stringArgs.length) {
                        case 2:
                            options.add(String.valueOf((int) player.getLocation().getX()));
                            break;
                        case 3:
                            options.add(String.valueOf((int) player.getLocation().getZ()));
                            break;
                        case 4:
                            options = colourList;
                            break;
                    }
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                PluginPlayerCompassPoints pluginPlayerCompassPoints = new PluginPlayerCompassPoints();
                for (CompassPoint compassPoint : pluginPlayerCompassPoints.get(player)) {
                    options.add(compassPoint.getLabel());
                }
            }
            return copyPartialMatches(args,options);
        }
        return null;
    }

    private List<String> copyPartialMatches(String[] args, List<String> options) {
        return StringUtil.copyPartialMatches(args[args.length - 1].toLowerCase(Locale.ROOT),options, new ArrayList<>());
    }

    private String argsToString(String[] args) {
        StringBuilder argsToString = new StringBuilder();
        for (String arg : args) {
            argsToString.append(arg).append(" ");
        }
        return argsToString.toString();
    }
}
