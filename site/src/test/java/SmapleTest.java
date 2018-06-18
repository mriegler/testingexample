import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmapleTest {
    @Test
    void givenAString_Stringxlength_itReturnsTheLength() {
        assertTrue("asd".length() == 3);
    }
}
