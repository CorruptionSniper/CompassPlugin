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

            List<String> settings = Arrays.asList("compassToggle","guiScale","fov","screenCoverage","dimensions");

            if (args.length == 1) {
                options.add("format");
                options.add("restoreDefaults");
                options.add("info");
            }
            if (args.length > 1 & args[0].equalsIgnoreCase("info")) {
                options.addAll(settings);
            } else {
                for (String setting : settings) {
                    options.add(setting + ":");
                }
            }
            options = copyPartialMatches(args,options);

            if (options.size() == 1 & !(args[0].equalsIgnoreCase("info"))) {
                switch (options.get(0).toLowerCase(Locale.ROOT).split(":",2)[0]) {
                    case "compasstoggle":
                        options = extendSetting("compassToggle:", Arrays.asList("true", "false"));
                        break;
                    case "guiscale":
                        options = extendSetting("guiScale:", Arrays.asList("1", "2", "3", "4"));
                        break;
                    case "fov":
                        options = extendSetting("fov:", Arrays.asList("70","80","90"));
                        break;
                    case "screencoverage":
                        options = extendSetting("screenCoverage:", Arrays.asList("1", "0.9", "0.8"));
                        break;
                    case "dimensions":
                        options = extendSetting("dimensions:", Arrays.asList("1920x1080", "1024x768"));
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

    private  List<String> extendSetting(String prefix, List<String> suffixList) {
        List<String> list = new ArrayList<>();

        for (String suffix : suffixList) {
            list.add(prefix + suffix);
        }
        return list;
    }
}
