package gg.horizonnetwork.HNDungeons.utils;

import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonMob;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtil {

    public static Pattern HEX_PATTERN = Pattern.compile("&(#[A-Fa-f0-9]{6})");
    public static char COLOR_CHAR = ChatColor.COLOR_CHAR;

    public static String translateHexColorCodes(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

    public static void sendMessage(Player p, String msg) {
        p.sendMessage(ChatUtil.translateHexColorCodes(ChatColor.translateAlternateColorCodes('&', msg)));
    }

    public static void sendPathMessage(Player p, String path) {
        ConfigurationSection section = Objects.requireNonNull(ConfigManager.getMessages()).getConfigurationSection("messages");
        if(section == null) {
            Logger.error("Lang field not found: &cmessages."+ path + "&e.");
            return;
        }
        String msg = section.getString(path);
        if(msg != null) {
            if(msg.startsWith("MemorySection")) {
                Logger.error("Lang field not found: &cmessages."+path + "&e.");
                p.sendMessage(ChatUtil.translateHexColorCodes(ChatColor.translateAlternateColorCodes('&',
                        "&cAn error occured while fetching the lang file, please contact a administrator")));
                return;
            }
            p.sendMessage(ChatUtil.translateHexColorCodes(ChatColor.translateAlternateColorCodes('&', msg)));
        } else {
            Logger.error("Lang field not found: &cmessages."+path + "&e.");
        }
    }

    public static String getPathMessage(String path) {
        ConfigurationSection section = Objects.requireNonNull(ConfigManager.getMessages()).getConfigurationSection("messages");
        if(section == null) {
            Logger.error("Lang field not found: &cmessages."+ path + "&e.");
            return null;
        }
        String str = section.getString(path);
        if(str == null) {
            Logger.error("Lang field not found: &cmessages."+ path + "&e.");
        }
        return str == null ? "&cAn error occured while fetching the lang file, please contact a administrator" : str;
    }

    public static void sendPartyMessage(DungeonInstance d, String msg) {
        for(DungeonPlayer p : d.getParty().getPlayers()) {
            if(p.getPlayer() != null) {
                ChatUtil.sendMessage(p.getPlayer(), msg);
            }
        }
    }

    public static String formatDungeonMobsName(DungeonMob m, String text) {
        return ChatUtil.translateHexColorCodes(ChatColor.translateAlternateColorCodes('&',
                text
                        .replaceAll(
                                "%level%",
                                String.valueOf(m.getInstance().getLevel())
                        )
                        .replaceAll(
                                "%id%",
                                m.getInstance().getId().toString()
                        )
                        .replaceAll(
                                "%host%",
                                String.valueOf(m.getInstance().getHost().getOfflinePlayer().getUniqueId())
                        )
        ));
    }

    public static String formatGeneralInstanceMessage(Player p, DungeonInstance i, String worldType, String msg) {
        return ChatUtil.translateHexColorCodes(ChatColor.translateAlternateColorCodes('&',
                msg
                        .replaceAll(
                                "%level%",
                                String.valueOf(i.getLevel())
                        )
                        .replaceAll(
                                "%id%",
                               i.getId().toString()
                        )
                        .replaceAll(
                                "%player%",
                                p.getName()
                        )
                        .replaceAll(
                                "%dungeonName%",
                                worldType
                        )
        ));
    }

    public static String formatGeneralInstanceMessage(Player p, DungeonInstance i, String msg) {
        return ChatUtil.translateHexColorCodes(ChatColor.translateAlternateColorCodes('&',
                msg
                        .replaceAll(
                                "%level%",
                                String.valueOf(i.getLevel())
                        )
                        .replaceAll(
                                "%id%",
                                i.getId().toString()
                        )
                        .replaceAll(
                                "%player%",
                                p.getName()
                        )
                        .replaceAll(
                                "%dungeonName%",
                                i.getWorld().getOriginalWorld().getName()
                        )
        ));
    }

    public static String formatGeneralMessage(Player p, String worldType, String msg) {
        return ChatUtil.translateHexColorCodes(ChatColor.translateAlternateColorCodes('&',
                msg
                        .replaceAll(
                                "%player%",
                                p.getName()
                        )
                        .replaceAll(
                                "%dungeonName%",
                                worldType
                        )
        ));
    }

    public static String formatWelcomeMessage(Player p, DungeonInstance i, String text) {
        return ChatUtil.translateHexColorCodes(ChatColor.translateAlternateColorCodes('&',
                text
                        .replaceAll(
                                "%level%",
                                String.valueOf(i.getLevel())
                        )
                        .replaceAll(
                                "%id%",
                                i.getId().toString()
                        )
                        .replaceAll(
                                "%player%",
                                p.getName()
                        )
                        .replaceAll(
                                "%host%",
                                Objects.requireNonNull(i.getHost().getOfflinePlayer().getName())
                        )
                        .replaceAll(
                                "%dungeonName%",
                                i.getWorld().getOriginalWorld().getName()
                        )
        ));
    }
}
