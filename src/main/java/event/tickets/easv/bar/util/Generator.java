package event.tickets.easv.bar.util;

import java.security.SecureRandom;

public class Generator {
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = UPPER_CASE.toLowerCase();
    private static final String DIGITS = "012345789";
    private static final String SPECIAL_CHARACTERS = "!@#$%&*()_+-=[]?";
    private static final String ALL_ALLOWED_CHARS = UPPER_CASE + LOWER_CASE + DIGITS + SPECIAL_CHARACTERS;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generatePassword(int length) {
        if (length < 4) throw new IllegalArgumentException("Password length must be at least 4 characters long");

        // get at least one of each character category
        StringBuilder password = new StringBuilder(length);
        password.append(UPPER_CASE.charAt(RANDOM.nextInt(UPPER_CASE.length())));
        password.append(LOWER_CASE.charAt(RANDOM.nextInt(LOWER_CASE.length())));
        password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(RANDOM.nextInt(SPECIAL_CHARACTERS.length())));

        // pick from random categories for remaining
        for (int i = 4; i < length; i++) {
            password.append(ALL_ALLOWED_CHARS.charAt(RANDOM.nextInt(ALL_ALLOWED_CHARS.length())));
        }

        // le shuffle here
        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = RANDOM.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }

        return new String(passwordArray);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(generatePassword(8));
        }
    }
}
