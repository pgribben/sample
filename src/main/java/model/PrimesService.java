package model;

import java.util.List;

/**
 * Created by pgribben on 05/03/2016.
 */
public interface PrimesService {
    /**
     * Get list of all the first 'maxPrimesCount' prime numbers in the range [fromValue,maxvalue]
     *
     * @param fromValue
     * @param maxValue
     * @param maxPrimesCount
     * @return List of Integer primes
     */
    List<Integer> getPrimesInDomain(int fromValue, int maxValue, int maxPrimesCount);

    /**
     * Test whether a number is prime
     *
     * @param value
     * @return true if supplied value is a prime number
     */
    boolean isPrime(int value);
}
