package rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * The Jersey Rest client overwrites the WebApplicationException message value. Therefore RestException has been
 * created to retain the message provided by the service code.
 *
 * Created by pgribben on 07/03/2016.
 */
public class RestException extends WebApplicationException {
    private final String serviceMessage;
    public RestException(final String message, final Response.Status status) {
        // hold original message in custom StatusType object..
        super(message, Response.status(new Response.StatusType() {
            final int statusCode = status.getStatusCode();
            final String reasonPhrase = message;

            @Override
            public int getStatusCode() {
                return statusCode;
            }
            @Override
            public Response.Status.Family getFamily() {
                return Response.Status.Family.familyOf(statusCode);
            }
            @Override
            public String getReasonPhrase() {
                return reasonPhrase;
            }
        }).build());
        serviceMessage = message;
    }
}
