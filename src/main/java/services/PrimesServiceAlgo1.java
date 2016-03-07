package services;

import model.PrimesService;

import java.util.ArrayList;
import java.util.List;
import static services.PrimeUtils.*;
/**
 * This implementation uses a seive algorithm mark off prime multiples from the value domain space.
 * The remaining unmarked values are therefore primes
 *
 * Created by pgribben on 03/03/2016.
 */
public class PrimesServiceAlgo1 implements PrimesService {
    public List<Integer> getPrimesInDomain(int fromValue, int maxValue, int maxPrimesCount) {
        if (maxValue<fromValue) {
            throw new IllegalArgumentException(
                    String.format("fromValue %d must be less than or equal to maxValue %d",
                            fromValue, maxValue));
        }
        testNaturalNumber(fromValue);
        List<Integer> primes = new ArrayList<>();

        if (maxPrimesCount == 0) {
            // return empty list
            return primes;
        }

        boolean[] seive = new boolean[maxValue+1];
        for(int i=2; i<=maxValue; i++) {
            if (!seive[i]) {
                if (i>=fromValue) {
                    primes.add(i);
                    if (primes.size()>=maxPrimesCount) {
                        break;
                    }
                }
                for (int j=i; j<=maxValue; j+=i) {
                    seive[j] = true;
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
