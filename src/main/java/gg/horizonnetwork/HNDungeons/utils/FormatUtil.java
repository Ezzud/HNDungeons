package gg.horizonnetwork.HNDungeons.utils;


import gg.horizonnetwork.HNDungeons.api.DungeonMob;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class FormatUtil {
    private static final Pattern REGEX = compile("(\\d+(?:\\.\\d+)?)([KMG]?)");
    private static final String[] KMG = new String[] {"", "K", "M", "G", "T", "T", "T", "T", "T", "T", "Q"};

    public static String formatDbl(double d) {
        double d2 = round(d, 2);
        int i = 0;
        while (d2 >= 1000) { i++; d2 /= 1000; }
        return round(d2, 2) + KMG[i];
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String formatCommand(DungeonMob dungeonMob, Player player, String command) {
        double totalFactor = dungeonMob.getRewardMultiplier() == 1.0 && dungeonMob.getInstance().getLevelMultiplier() == 1.0 ? 1.0 : dungeonMob.getRewardMultiplier() + dungeonMob.getInstance().getLevelMultiplier();
        String newCommand = command
                .replaceAll("%player%", player.getName())
                .replaceAll("%mobName%", dungeonMob.getName())
                .replaceAll("%boostFactor%", String.valueOf(dungeonMob.getRewardMultiplier()))
                .replaceAll("%levelFactor%", String.valueOf(dungeonMob.getInstance().getLevelMultiplier()))
                .replaceAll("%totalFactor%", String.valueOf(totalFactor));
        for(String word : newCommand.split(" ")) {
            // Usage %rdm_10-10000%
            if(word.startsWith("%rdm_") && word.endsWith("%")) {
                String stripped = word.strip();
                stripped = stripped.replaceFirst("rdm_", "").replaceAll("%", "");
                int minValue = Integer.parseInt(stripped.split("-")[0]);
                int maxValue = Integer.parseInt(stripped.split("-")[1]);
                int randomValue = RandomUtil.getRandomNumberInRange(minValue, maxValue);
                double boostedValue = randomValue * totalFactor;
                newCommand = newCommand.replace(word, String.valueOf(boostedValue));
            }
        }
        return newCommand;
    }
}
