package me.corruptionsniper.compass.tabCompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SettingsTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player & !(args[0].equalsIgnoreCase("restoredefaults") | args[0].equalsIgnoreCase("format"))) {

            List<String> options = new ArrayList<>();

            if (args.length == 1) {
                options.add("format");
                options.add("restoreDefaults");
            }
            options.add("compassToggle:");
            options.add("guiScale");
            options.add("fov:");
            options.add("screenCoverage:");
            options.add("dimensions:");
            options = copyPartialMatches(args,options);

            if (options.size() == 1) {
                switch (options.get(0).toLowerCase(Locale.ROOT).split(":",2)[0]) {
                    case "compasstoggle":
                        options = extendCompleter("compassToggle:", Arrays.asList("true", "false"));
                        break;
                    case "guiscale":
                        options = extendCompleter("guiScale:", Arrays.asList("1", "2", "3", "4"));
                        break;
                    case "fov":
                        options = extendCompleter("fov:", Arrays.asList("70"));
                        break;
                    case "screencoverage":
                        options = extendCompleter("screenCoverage:", Arrays.asList("1", "0.8", "0.75"));
                        break;
                    case "dimensions":
                        options = extendCompleter("dimensions:", Arrays.asList("1920x1080", "1024x768"));
                        break;
                }
            }
            return options;

        }
        return null;
    }

    private List<String> copyPartialMatches(String[] args, List<String> list) {
        return StringUtil.copyPartialMatches(args[args.length - 1].toLowerCase(Locale.ROOT).split(":",2)[0],list, new ArrayList<>());
    }

    private  List<String> extendCompleter(String prefix, List<String> suffixList) {
        List<String> list = new ArrayList<>();

        for (String suffix : suffixList) {
            list.add(prefix + suffix);
        }
        return list;
    }
}
