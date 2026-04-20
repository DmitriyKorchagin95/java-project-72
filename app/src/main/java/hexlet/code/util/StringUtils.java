package hexlet.code.util;

public final class StringUtils {

    private StringUtils() {
    }

    public static String truncate(String value, int limit) {

        if (value == null) {
            return null;
        }

        return value.length() > limit
                ? String.format("%s%s", value.substring(0, limit), "...")
                : value;
    }
}
