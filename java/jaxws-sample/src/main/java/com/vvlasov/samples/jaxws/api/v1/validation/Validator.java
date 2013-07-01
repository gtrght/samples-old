package com.vvlasov.samples.jaxws.api.v1.validation;

import com.vvlasov.samples.jaxws.api.v1.exception.ApiValidationException;

/**
 * User: Vasily Vlasov
 * Date: 22.04.13
 */
public interface Validator {

    public boolean validate(Object object) throws ApiValidationException;

}
