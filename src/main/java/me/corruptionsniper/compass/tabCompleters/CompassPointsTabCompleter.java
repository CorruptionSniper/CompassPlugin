package me.corruptionsniper.compass.tabCompleters;

import me.corruptionsniper.compass.CommandUtil;
import me.corruptionsniper.compass.compassPoints.CompassPoint;
import me.corruptionsniper.compass.compassPoints.PlayerCompassPoints;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CompassPointsTabCompleter implements TabCompleter {
    CommandUtil commandUtil = new CommandUtil();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            List<String> options = new ArrayList<>();

            if (args.length == 1) {
                options.add("format");
                options.add("restoreDefaults");
                options.add("add");
                options.add("remove");
            } else {
                switch (args[0].toLowerCase(Locale.ROOT)) {
                    case "add":
                        String[] compassPointArgs = commandUtil.join(Arrays.asList(args), " ").substring(args[0].length() + 1).split(";");
                        String[] lastCompassPointArg = compassPointArgs[compassPointArgs.length - 1].split(",");
                        if (lastCompassPointArg.length == 2) {
                            options.add("direction");
                            options.add("coordinate");
                        } else if (lastCompassPointArg.length > 2) {
                            switch (lastCompassPointArg[1].trim().toLowerCase(Locale.ROOT)) {
                                case "direction":
                                    switch (lastCompassPointArg.length) {
                                        case 3:
                                            options.add(String.valueOf((int) player.getLocation().getYaw() + 180));
                                            break;
                                        case 4:
                                            options = commandUtil.colourList();
                                            break;
                                    }
                                    break;
                                case "coordinate":
                                    switch (lastCompassPointArg.length) {
                                        case 3:
                                            options.add(String.valueOf((int) player.getLocation().getX()));
                                            break;
                                        case 4:
                                            options.add(String.valueOf((int) player.getLocation().getZ()));
                                            break;
                                        case 5:
                                            options = commandUtil.colourList();
                                            break;
                                    }
                                    break;
                            }
                        }
                        break;
                    case "remove":
                        PlayerCompassPoints playerCompassPoints = new PlayerCompassPoints();
                        for (CompassPoint compassPoint : playerCompassPoints.get(player)) {
                            options.add(compassPoint.getLabel());
                        }
                        break;
                }
            }

            return commandUtil.copyPartialMatches(args,options);
        }
        return null;
    }
}
