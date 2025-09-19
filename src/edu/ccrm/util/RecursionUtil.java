package edu.ccrm.util;

import java.io.File;

/**
 * Recursion utility to compute directory size (demonstrates recursion).
 */
public class RecursionUtil {
    public static long directorySize(File dir) {
        if (dir == null) return 0;
        if (!dir.exists()) return 0;
        if (dir.isFile()) return dir.length();
        long total = 0;
        File[] files = dir.listFiles();
        if (files == null) return 0;
        for (File f : files) total += directorySize(f);
        return total;
    }
}
