package org.example.murilo.ordemservico.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    public static boolean isEmpty(final String string) {
        return string == null || string.isBlank();
    }

    public static boolean containsSpecialCharacter(final String string) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }

    public static boolean isValidUsername(final String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }

        if (username.length() < 3 || username.length() > 30){
            return false;
        }

        if (username.contains(" ")) {
            return false;
        }

        if (StringUtils.containsSpecialCharacter(username)) {
            return false;
        }

        return true;
    }

    public static boolean isValidName(final String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }

        if (name.length() < 3 || name.length() > 30){
            return false;
        }

        if (name.contains(" ")) {
            return false;
        }

        if (StringUtils.containsSpecialCharacter(name)) {
            return false;
        }

        return true;
    }

    public static boolean isValidPassword(final String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }

        if (password.length() < 4 || password.length() > 10){
            return false;
        }

        if (password.contains(" ")) {
            return false;
        }

        if (StringUtils.containsSpecialCharacter(password)) {
            return false;
        }

        return true;
    }
}
