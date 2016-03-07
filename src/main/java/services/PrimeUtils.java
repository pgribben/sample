package services;

/**
 * Set of utility methods for use by prime service implementations
 *
 * Created by pgribben on 07/03/2016.
 */
public class PrimeUtils {
    /**
     * Test whether value is a natural number
     * @param value
     * @throws IllegalArgumentException if number is <= 0
     */
    public static void testNaturalNumber(int value) {
        if (value<1) {
            throw new IllegalArgumentException(
                    String.format("Value %d must be a natural number", value));
        }
    }

    /**
     * Test whether supplied value is a prime number
     *
     * @param value
     * @return true if number is prime, otherwise false
     */
    public static boolean isPrimeValue(int value) {
        int maxCheckVal = (int)Math.ceil(Math.sqrt(value));
        for (int f=2; f<=maxCheckVal; f++) {
            if (value % f == 0) {
                return false;
            }
        }
        return true;
    }
}
