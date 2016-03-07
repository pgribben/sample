package rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

/**
 * Service execution
 *
 * Created by pgribben on 07/03/2016.
 */
public interface RequestExecutor<T> {
    T execute();
}
