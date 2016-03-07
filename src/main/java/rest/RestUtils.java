package rest;

import org.slf4j.Logger;

import javax.ws.rs.core.Response;

/**
 * Collection of utility classes for use by Rest resources
 *
 * Created by pgribben on 07/03/2016.
 */
public class RestUtils {

    /**
     * Invoke the supplied executor on the current thread, with REST-oriented exception handling
     *
     * @param logger caller's logger to which errors/warnings will be reported.
     * @param errMessage prefix to any exception messages thrown from execution (or null if none required)
     * @param errResponseStatus HTTP status code to generate on error.
     * @param executor the executor to invoke
     * @param <T>  The type that the executor returns on success
     * @return The executor's return value
     * @throws RestException
     */
    public static <T> T process(Logger logger, String errMessage, Response.Status errResponseStatus, RequestExecutor<T> executor) {
        try {
            return executor.execute();
        } catch (Throwable t) {
            String message = (errMessage != null ? errMessage + ": " + t.getMessage() : t.getMessage());
            logger.error(t.toString());
            throw new RestException(message, errResponseStatus);
        }
    }
}
