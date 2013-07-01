package com.vvlasov.samples.jaxws.api.v1.exception;

import javax.xml.ws.WebFault;

/**
 * User: Vasily Vlasov
 * Date: 22.04.13
 */
@WebFault
public class ApiValidationException extends RuntimeException {
    public ApiValidationException() {
    }

    public ApiValidationException(Throwable cause) {
        super(cause);
    }

    public ApiValidationException(String message) {
        super(message);
    }

    public ApiValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
