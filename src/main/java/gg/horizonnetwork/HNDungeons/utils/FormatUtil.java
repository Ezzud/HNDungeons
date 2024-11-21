package gg.horizonnetwork.HNDungeons.utils;


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
}
