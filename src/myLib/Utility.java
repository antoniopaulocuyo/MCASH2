package myLib;

public class Utility {

    public static boolean validateInt(String user_int) {
        if (user_int == null || user_int.trim().isEmpty()) {
            return false;
        }

        try {
            Integer.parseInt(user_int.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean validateString(String user_string) {
        return user_string != null && !user_string.trim().isEmpty();
    }

    public static boolean validateDouble(String user_double) {
        if (user_double == null || user_double.trim().isEmpty()) {
            return false;
        }

        try {
            Double.parseDouble(user_double.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

