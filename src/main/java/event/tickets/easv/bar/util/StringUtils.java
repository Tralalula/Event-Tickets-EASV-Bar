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

    /**
     * Consumes a first name and a last name and produces a new string with the initials of the names.
     * If the last name is null or empty, it uses the first two letters of the first name.
     *
     * @return a new string with the initials of the names.
     */
    public static String initialize(String firstName, String lastName) {
        String initials;
        if (lastName == null || lastName.isEmpty()) {
            initials = firstName.length() > 1 ? firstName.substring(0, 2) : firstName;
        } else {
            initials = firstName.substring(0, 1) + lastName.substring(0, 1);
        }
        return initials.toUpperCase();
    }
}
