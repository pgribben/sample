package model;

import com.google.common.base.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Created by pgribben on 06/03/2016.
 */
@Path("/primes")
public interface PrimesResource {
    /**
     * Test whether candidate value is a prime
     * @param candidate
     * @return true if prime otherwise false
     */
    @Path("{value}/valid")
    @GET
    boolean validatePrime(@PathParam("value") Integer candidate);

    /**
     * Compute lowest prime number that is greater than the supplied reference value
     * @param referenceValue
     * @return next prime number ascending from the reference value
     */
    @Path("nextPrime")
    @GET
    Integer getNextPrime(@QueryParam("ref") Optional<Integer> referenceValue);

    /**
     * Compute list of all the first 'maxPrimesCount' prime numbers in the range [fromValue,maxvalue]
     * @param startRange lowest inclusive value in range of candidate values.
     * @param endRange highest inclusive value in range of candidate values.
     * @param maxResultSize  maximum number of prime values to return
     * @return List of primes
     */
    @Path("bounded")
    @GET
    IntList getPrimesInRange(@QueryParam("start") Integer startRange,
                             @QueryParam("end") Integer endRange,
                             @QueryParam("maxResultSize") Optional<Integer> maxResultSize);
}
