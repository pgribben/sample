package rest;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import io.dropwizard.testing.junit.ResourceTestRule;
import model.IntList;
import model.PrimesResource;
import model.PrimesService;
import org.junit.ClassRule;
import org.junit.Test;
import rest.resources.PrimesResourceImpl;
import services.PrimesServiceAlgo1;
import services.PrimesServiceAlgo2;
import services.PrimesServiceAlgo3;

import javax.ws.rs.BadRequestException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


public class PrimesResourceTest {

    private static Injector testInjector1 = Guice.createInjector(new AbstractModule() {
        @Override
        protected void configure() {
            bind(PrimesService.class).to(PrimesServiceAlgo1.class);
            bind(Integer.class).annotatedWith(Names.named("maxPrimes")).toInstance(1000);
            bind(PrimesResource.class).to(PrimesResourceImpl.class);
        }
    });
    private static Injector testInjector2 = Guice.createInjector(new AbstractModule() {
        @Override
        protected void configure() {
            bind(PrimesService.class).to(PrimesServiceAlgo2.class);
            bind(Integer.class).annotatedWith(Names.named("maxPrimes")).toInstance(1000);
            bind(PrimesResource.class).to(PrimesResourceImpl.class);
        }
    });
    private static Injector testInjector3 = Guice.createInjector(new AbstractModule() {
        @Override
        protected void configure() {
            bind(PrimesService.class).to(PrimesServiceAlgo3.class);
            bind(Integer.class).annotatedWith(Names.named("maxPrimes")).toInstance(1000);
            bind(PrimesResource.class).to(PrimesResourceImpl.class);
        }
    });

    @ClassRule
    public static final ResourceTestRule resources1 = ResourceTestRule.builder()
            .addResource(testInjector1.getInstance(PrimesResource.class))
            .build();

    @ClassRule
    public static final ResourceTestRule resources2 = ResourceTestRule.builder()
            .addResource(testInjector2.getInstance(PrimesResource.class))
            .build();

    @ClassRule
    public static final ResourceTestRule resources3 = ResourceTestRule.builder()
            .addResource(testInjector3.getInstance(PrimesResource.class))
            .build();


    @Test
    public void testGetNextPrime() throws Exception {
        assertThat(resources1.client().target("/primes/nextPrime").queryParam("ref", 20).request().get(Integer.class))
                .isEqualTo(new Integer(23));
    }

    @Test(expected = BadRequestException.class)
    public void testGetNextPrime_badRefValue() throws Exception {
        resources1.client().target("/primes/nextPrime").queryParam("ref", -5).request().get(Integer.class);
    }

    @Test(expected = BadRequestException.class)
    public void testGetBadRequest() throws Throwable {
        // this should fail because start>end value
        resources1.client().target("/primes/bounded").queryParam("start", 10).queryParam("end", 5).request().get(IntList.class);
        fail("Should have received a ProcessingException");
    }

    @Test
    public void testGetPrimesInRange20to100() throws Exception {
        IntList result1 = resources1.client().target("/primes/bounded")
                .queryParam("start", 20)
                .queryParam("end", 100)
                .queryParam("maxResultSize", 10)
                .request().get(IntList.class);
        List<Integer> primes1 = result1.getData();

        IntList result2 = resources2.client().target("/primes/bounded")
                .queryParam("start", 20)
                .queryParam("end", 100)
                .queryParam("maxResultSize", 10)
                .request().get(IntList.class);
        List<Integer> primes2 = result2.getData();

        assertThat(compareLists(primes1, primes2)).isTrue();

        assertThat(primes1.size()).isLessThanOrEqualTo(10);
        assertThat(primes1).endsWith(61);
        assertThat(primes1).startsWith(23);
    }

    @Test
    public void testMultiThreadedPrimeGenerator() throws Exception {
        IntList result1 = resources3.client().target("/primes/bounded")
                .queryParam("start", 20)
                .queryParam("end", 1200000)
                .queryParam("maxResultSize", 1200000)
                .request().get(IntList.class);
        List<Integer> primes1 = result1.getData();
        System.out.println(primes1.size());
    }

    private boolean compareLists(List<Integer> list1, List<Integer> list2) {
        if (list1 == null && list2 == null) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }

        for (int i=0; i<list1.size(); i++) {
            if (!list1.get(i).equals(list2.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testGetPrimesInRange9900to100000() throws Exception {
        IntList result = resources1.client().target("/primes/bounded")
                .queryParam("start", 9900)
                .queryParam("end", 10000)
                .request().get(IntList.class);
        List<Integer> primes = result.getData();
        assertThat(primes).startsWith(9901);
        assertThat(primes).endsWith(9973);
    }

    @Test
    public void testIsPrime() {
        assertThat(resources1.client().target("/primes/9901/valid").request().get(Boolean.class)).isTrue();
        assertThat(resources1.client().target("/primes/2001/valid").request().get(Boolean.class)).isFalse();
    }
}