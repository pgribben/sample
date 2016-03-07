import org.junit.Test;
import services.PrimesServiceAlgo1;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class PrimesTest {

    @Test
    public void testGetPrimesInDomain() throws Exception {
        PrimesServiceAlgo1 primes = new PrimesServiceAlgo1();
        List<Integer> values = primes.getPrimesInDomain(2,4000,20);
        assertTrue(20 == values.size());
        assertTrue(2 == values.get(0));
        assertTrue(71 == values.get(19));
    }
}