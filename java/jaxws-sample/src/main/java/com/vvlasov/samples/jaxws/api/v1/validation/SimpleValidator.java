package com.vvlasov.samples.jaxws.api.v1.validation;

import com.vvlasov.samples.jaxws.api.v1.exception.ApiValidationException;
import com.vvlasov.samples.jaxws.api.v1.soap.AddRequest;

/**
 * User: Vasily Vlasov
 * Date: 22.04.13
 */
public class SimpleValidator implements Validator {
    @Override
    public boolean validate(Object object) throws ApiValidationException {

        if(object instanceof AddRequest){
            AddRequest request = (AddRequest) object;
            if (request.getOp1() == null)
                throw new ApiValidationException("Operator 1 is null.");
            if (request.getOp2() == null)
                throw new ApiValidationException("Operator 2 is null.");

        }

        return true;
    }
}
