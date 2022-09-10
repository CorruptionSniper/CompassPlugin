package me.corruptionsniper.compass.tabCompleters;

import me.corruptionsniper.compass.compassPoints.CompassPoint;
import me.corruptionsniper.compass.compassPoints.PluginPlayerCompassPoints;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
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
                if (args.length == 2) {
                    options.add("direction");
                    options.add("coordinate");
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
}
