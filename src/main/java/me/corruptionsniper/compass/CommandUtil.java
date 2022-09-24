package me.corruptionsniper.compass;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CommandUtil {
    public String join(List<String> list, String separator) {
        StringBuilder builder = new StringBuilder();
        for (String item : list) {
            builder.append(item).append(separator);
        }
        return builder.toString();
    }

    public void listingMessage(Player player, ChatColor messageColour, ChatColor listingColour, String prefixMessage, List<String> listing, String suffixMessage) {
        if (!listing.isEmpty()) {
            StringBuilder message = new StringBuilder(messageColour + prefixMessage + listingColour);
            for (String error : listing) {
                message.append(" '").append(error).append("',");
            }
            message.append("\n").append(messageColour).append(suffixMessage);
            player.sendMessage(message.toString());
        }
    }

    public List<String> copyPartialMatches(String[] args, List<String> options) {
        return StringUtil.copyPartialMatches(args[args.length - 1].toLowerCase(Locale.ROOT),options, new ArrayList<>());
    }

    public List<String> colourList() {
        return Arrays.asList("black", "dark blue", "dark green", "dark aqua", "dark red", "dark purple", "gold", "gray", "dark gray", "blue", "green", "aqua", "red", "light purple", "yellow", "white");
    }

    public String setColour(String string, ChatColor color) {
        ChatColor standardColour = ChatColor.WHITE;
        return color + string + standardColour;
    }
}
