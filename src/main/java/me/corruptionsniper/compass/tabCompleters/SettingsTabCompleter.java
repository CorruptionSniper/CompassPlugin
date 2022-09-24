package me.corruptionsniper.compass.tabCompleters;

import me.corruptionsniper.compass.CommandUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class SettingsTabCompleter implements TabCompleter {

    CommandUtil commandUtil = new CommandUtil();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player) {
            List<String> options = new ArrayList<>();

            List<String> settings = Arrays.asList("compassToggle","guiScale","fov","screenCoverage","dimensions");

            if (args.length == 1) {
                options.add("format");
                options.add("restoreDefaults");
                options.add("info");
            }
            for (String setting: settings) {
                options.add(setting + ":");
            }
            options = commandUtil.copyPartialMatches(args, options);

            switch (args[0]) {
                case "format":
                case "restoreDefaults":
                    options = new ArrayList<>();
                    break;
                case "info":
                    options = commandUtil.copyPartialMatches(args, settings);
                    break;
                default:
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
            }
            return options;
        }
        return null;
    }

    private  List<String> extendSetting(String prefix, List<String> suffixList) {
        List<String> list = new ArrayList<>();

        for (String suffix : suffixList) {
            list.add(prefix + suffix);
        }
        return list;
    }
}
