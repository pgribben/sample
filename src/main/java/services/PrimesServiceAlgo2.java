package services;

import model.PrimesService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static services.PrimeUtils.*;

/**
 * This implementation iterates through the range of values testing each value for prime.
 *
 * Created by pgribben on 03/03/2016.
 */
public class PrimesServiceAlgo2 implements PrimesService {
    public List<Integer> getPrimesInDomain(int fromValue, int maxValue, int maxPrimesCount) {
        if (maxValue<fromValue) {
            throw new IllegalArgumentException(
                    String.format("fromValue %d must be less than or equal to maxValue %d",
                            fromValue, maxValue));
        }
        testNaturalNumber(fromValue);

        List<Integer> primes = new ArrayList<>();

        if (maxPrimesCount == 0 || maxValue<fromValue) {
            // return empty list
            return primes;
        }
        if (fromValue <= 2) {
            primes.add(2);
            if (maxPrimesCount==1) {
                // return single value
                return primes;
            }
        }

        // adjust start value to be odd as we will not test even values
        fromValue += (fromValue % 2 == 0 ? 1 : 0);

        // Find primes in defined set of natural numbers
        for(int i=fromValue; i<=maxValue; i+=2) {
            if (isPrime(i)) {
                primes.add(i);
                if (primes.size()>=maxPrimesCount) {
                    break;
                }
            }
        }
        return primes;
    }

    @Override
    public boolean isPrime(int value) {
        return isPrimeValue(value);
    }

}
