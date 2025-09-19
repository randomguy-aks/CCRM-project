package edu.ccrm.util;

/**
 * Small validators utility.
 */
public class Validators {
    public static boolean isValidEmail(String e) {
        if (e == null) return false;
        return e.contains("@") && e.contains(".");
    }
}
