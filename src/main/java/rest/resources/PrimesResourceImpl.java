package rest.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import model.IntList;
import model.PrimesResource;
import model.PrimesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest.RequestExecutor;

import javax.ws.rs.core.Response;
import java.util.List;
import static rest.RestUtils.process;

/**
 * Created by pgribben on 05/03/2016.
 */
public class PrimesResourceImpl implements PrimesResource {
    private PrimesService service;
    private final int maxPrimesCount;
    final static Logger logger = LoggerFactory.getLogger(PrimesResourceImpl.class);

    @Inject
    public PrimesResourceImpl(@Named("maxPrimes") final int maxPrimesCount, PrimesService primesService) {
        this.maxPrimesCount = maxPrimesCount;
        this.service = primesService;
    }

    @Override
    public boolean validatePrime(Integer candidate) {
        return service.isPrime(candidate);
    }

    @Override
    public Integer getNextPrime(Optional<Integer> referenceValue) {
        return process(logger, null, Response.Status.BAD_REQUEST, new RequestExecutor<Integer>() {
            @Override
            public Integer execute() {
                int fromValue = referenceValue.or(1);
                return service.getPrimesInDomain(fromValue, fromValue + 1000000, 1).get(0);
            }
        });

    }

    @Override
    public IntList getPrimesInRange(Integer startRange,
                                    Integer endRange,
                                    Optional<Integer> maxResultSize) {
        return process(logger, String.format("Failed for arguments: startRange=%d, endRange=%d", startRange, endRange),
            Response.Status.BAD_REQUEST, new RequestExecutor<IntList>() {
                @Override
                public IntList execute() {
                    List<Integer> primes = service.getPrimesInDomain(startRange, endRange, maxResultSize.or(maxPrimesCount));
                    return new IntList(primes);
                }
            });
    }


}
