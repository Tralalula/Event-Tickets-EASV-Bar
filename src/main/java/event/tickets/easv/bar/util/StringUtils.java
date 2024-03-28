package event.tickets.easv.bar.util;

/**
 * Contains utility methods for manipulating strings.
 */
public class StringUtils {
    /**
     * Consumes a string str and produces a new capitalized string.
     *
     * @param str the string to be capitalized. Can be null or empty.
     * @return a new capitalized string.<br>
     *         If input string is null or empty, it returns null or empty, respectively.
     * <br><br>
     * Example usage:<br>
     *  - capitalize("hello") returns "Hello"<br>
     *  - capitalize("HELLO") returns "Hello"<br>
     *  - capitalize("h") returns "H"<br>
     *  - capitalize("") returns ""<br>
     *  - capitalize(null) returns null
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
