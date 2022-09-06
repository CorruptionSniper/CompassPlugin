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

            switch (args[args.length - 1].split(":",2)[0].toLowerCase(Locale.ROOT)) {
                case "compasstoggle":
                    options = extendArg("compassToggle:", Arrays.asList("true","false"));
                    return copyPartialMatches(args,options);
                case "guiscale":
                    options = extendArg("guiScale:", Arrays.asList("1","2","3","4"));
                    return copyPartialMatches(args,options);
                case "fov":
                    options = extendArg("fov:", Arrays.asList("70"));
                    return copyPartialMatches(args,options);
                case "screencoverage":
                    options = extendArg("screenCoverage:", Arrays.asList("1","0.8","0.75"));
                    return copyPartialMatches(args,options);
                case "dimensions":
                    options = extendArg("dimensions:", Arrays.asList("1920x1080","1024x768"));
                    return copyPartialMatches(args,options);
                default:
                    if (args.length == 1) {
                        options.add("format");
                        options.add("restoreDefaults");
                    }
                    options.add("compassToggle:");
                    options.add("guiScale");
                    options.add("fov:");
                    options.add("screenCoverage:");
                    options.add("dimensions:");
                    return copyPartialMatches(args,options);
            }

        }
        return null;
    }

    private List<String> copyPartialMatches(String[] args, List<String> list) {
        return StringUtil.copyPartialMatches(args[args.length - 1].toLowerCase(Locale.ROOT),list, new ArrayList<>());
    }

    private  List<String> extendArg(String prefix, List<String> suffixList) {
        List<String> list = new ArrayList<>();
        for (String suffix : suffixList) {
            list.add(prefix + suffix);
        }
        return list;
    }
}
