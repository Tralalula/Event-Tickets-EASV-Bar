package event.tickets.easv.bar.util;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    /**
     * Testing strategy for capitalize(String str)
     * The input is partitioned as follows:
     * 1. str is null
     * 2. str is empty
     * 3. str is a single character (lowercase, uppercase)
     * 4. str is multiple characters:
     *    a. all lowercase
     *    b. all uppercase
     *    c. capitalized
     *    d. reverse capitalized (first lower, rest upper)
     *    e. mixed case
     *    f. contains non-alphabetical characters (numbers, symbols, spaces)
     */
    @Test
    void capitalize() {
        assertThat(StringUtils.capitalize("")).isEmpty();
        assertThat(StringUtils.capitalize(null)).isNull();
        assertThat(StringUtils.capitalize("h")).isEqualTo("H");
        assertThat(StringUtils.capitalize("H")).isEqualTo("H");

        assertThat(StringUtils.capitalize("hello")).isEqualTo("Hello");
        assertThat(StringUtils.capitalize("HELLO")).isEqualTo("Hello");
        assertThat(StringUtils.capitalize("Hello")).isEqualTo("Hello");
        assertThat(StringUtils.capitalize("hELLO")).isEqualTo("Hello");
        assertThat(StringUtils.capitalize("heLlo")).isEqualTo("Hello");
        assertThat(StringUtils.capitalize("123 #¤/(/#¤/ #/ hElLo")).isEqualTo("123 #¤/(/#¤/ #/ hello");
    }
}