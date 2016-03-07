package primeservice.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by pgribben on 05/03/2016.
 */
public class ServiceConfiguration extends Configuration {

    @NotNull
    private int maxPrimesCount;

    @NotNull
    private int primesAlgoId;

    @JsonProperty
    public int getMaxPrimesCount() {
        return maxPrimesCount;
    }

    @JsonProperty
    public void setMaxPrimesCount(int maxPrimesCount) {
        this.maxPrimesCount = maxPrimesCount;
    }

    @JsonProperty
    public int getPrimesAlgoId() {
        return primesAlgoId;
    }

    @JsonProperty
    public void setPrimesAlgoId(int primesAlgoId) {
        this.primesAlgoId = primesAlgoId;
    }

}
