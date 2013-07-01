package com.vvlasov.samples.jaxws.api.v1;

import com.vvlasov.samples.jaxws.api.v1.exception.ApiValidationException;
import com.vvlasov.samples.jaxws.api.v1.soap.AddRequest;
import com.vvlasov.samples.jaxws.api.v1.soap.ArithmeticResponse;
import com.vvlasov.samples.jaxws.api.v1.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * User: Vasily Vlasov
 * Date: 22.04.13
 */
@WebService(endpointInterface = "com.vvlasov.samples.jaxws.api.v1.ArithmeticService")
public class ArithmeticServiceImpl implements ArithmeticService {
    @Autowired
    private Validator validator;

    @Override
    public ArithmeticResponse add(@WebParam(name = "Add") AddRequest request) throws ApiValidationException {
        //consider using the JSR 303 to check requests for null
        validator.validate(request);
        return new ArithmeticResponse(request.getOp1() + request.getOp2());
    }
}
