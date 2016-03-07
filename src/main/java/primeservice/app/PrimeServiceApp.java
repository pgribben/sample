package primeservice.app;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import model.PrimesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest.resources.PrimesResourceImpl;
import services.PrimesServiceAlgo1;
import services.PrimesServiceAlgo2;

/**
 * Created by pgribben on 05/03/2016.
 */
public class PrimeServiceApp extends Application<ServiceConfiguration> {
    final static Logger logger = LoggerFactory.getLogger(PrimeServiceApp.class);
    public static void main(String[] args) throws Exception {
        new PrimeServiceApp().run(args);
    }

    @Override
    public String getName() {
        return "services.Primes Service";
    }

    @Override
    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(ServiceConfiguration configuration, Environment environment) throws Exception {
        Injector injector = createInjector(configuration);
        environment.jersey().register(injector.getInstance(PrimesResourceImpl.class));
    }

    private Injector createInjector(ServiceConfiguration configuration) {
        return Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                Class primeAlgo = null;
                switch(configuration.getPrimesAlgoId()) {
                    case 1:
                        primeAlgo = PrimesServiceAlgo1.class;
                        break;
                    case 2:
                        primeAlgo = PrimesServiceAlgo2.class;
                        break;
                    default:
                        throw new IllegalArgumentException("primesAlgoId value must be between 1 and 2");
                }
                logger.info("Using primes implementation: " + primeAlgo.getSimpleName());
                bind(PrimesService.class).to(primeAlgo);
                bind(Integer.class).annotatedWith(Names.named("maxPrimes")).toInstance(configuration.getMaxPrimesCount());
            }
        });
    }
}
