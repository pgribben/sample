package services;

import model.PrimesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static services.PrimeUtils.isPrimeValue;
import static services.PrimeUtils.testNaturalNumber;

/**
 * This implementation uses simple iteration algorithm (as algo2) but divides the task into concurrent running
 * jobs, collating the results.
 *
 * Created by pgribben on 03/03/2016.
 */
public class PrimesServiceAlgo3 implements PrimesService {

    // Split the job into tasks for parallel execution (each one computing 1 million values)
    final int UNIT_WORK_SIZE = 300000;

    public List<Integer> getPrimesInDomain(int fromValue, int maxValue, int maxPrimesCount) {
        /*
         * First perform basic tests for input validity and trivial cases
         */
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

        // work size must be even number:
        //   The task start value must be odd so that the logic is correct
        //   Each parallel task starts from this requests 'fromValue' + multiple of UNIT_WORK_SIZE
        //
        assert UNIT_WORK_SIZE % 2 == 0;

        // set the number of parallel tasks to execute:
        // Naive split of the range of values - note higher ranges will do more work - so could be improved.
        int numTasks = Math.max(1, (int)Math.round((double)(maxValue - fromValue) / (double) UNIT_WORK_SIZE));

        // Resulting primes will be stored in an ordered concurrent Set 'results':
        final ConcurrentSkipListSet<Integer> results = new ConcurrentSkipListSet<>();

        // Define and kick off concurrent tasks
        BlockingQueue<Runnable> tasks = new ArrayBlockingQueue<Runnable>(numTasks);
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(numTasks, numTasks, 1, TimeUnit.MINUTES, tasks);
        for (int i=fromValue; i<maxValue; i+= UNIT_WORK_SIZE) {
            executorPool.execute(new PrimeGenerator(results, i, Math.min(i + UNIT_WORK_SIZE - 1, maxValue), maxPrimesCount));
        }

        try {
            // Wait for completion of all tasks
            executorPool.shutdown();
            // don't hold up this service by more than 1 minute
            executorPool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException("Error computing primes - possible timeout.");
        }

        // send back ascending ordered list of primes
        if (results.size()>maxPrimesCount) {
            // trim off excessive primes
            primes.addAll(results.subSet(0, true, maxPrimesCount, false));
        } else {
            primes.addAll(results);
        }
        return primes;
    }

    @Override
    public boolean isPrime(int value) {
        return isPrimeValue(value);
    }

    /**
     *  PrimeGenerator generates prime numbers within the range of int values provided, and writes the results
     *  to the provided concurrent Set. The task completes when either the range of values is exhausted or the
     *  primes Set size has reached maxPrimesCount
     */
    private static class PrimeGenerator implements Runnable {
        final static Logger logger = LoggerFactory.getLogger(PrimeGenerator.class);
        private final ConcurrentSkipListSet<Integer> primeSet;
        private final int minValue;
        private final int maxValue;
        private final int maxPrimesCount;

        private PrimeGenerator(ConcurrentSkipListSet<Integer> primeSet, int minValue, int maxValue, int maxPrimesCount) {
            System.out.println(String.format("Added generator for [%d,%d] range", minValue, maxValue));
            this.primeSet = primeSet;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.maxPrimesCount = maxPrimesCount;
        }

        @Override
        public void run() {
            System.out.println("Starting PrimeGenerator thread " + Thread.currentThread().getName());
            for(int i=minValue; i<=maxValue; i+=2) {
                if (isPrimeValue(i)) {
                    if (primeSet.size()>=maxPrimesCount) {
                        break; // done
                    }
                    primeSet.add(i);
                }
            }
            System.out.println("Terminating PrimeGenerator thread " + Thread.currentThread().getName());
        }
    }
}
