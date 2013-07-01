package com.vvlasov.samples.jaxws.api.v1;

import com.vvlasov.samples.jaxws.api.v1.exception.ApiValidationException;
import com.vvlasov.samples.jaxws.api.v1.soap.AddRequest;
import com.vvlasov.samples.jaxws.api.v1.soap.ArithmeticResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * User: Vasily Vlasov
 * Date: 22.04.13
 */

@WebService
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ArithmeticService {
    @WebMethod
    @WebResult(name = "OperationResult")
    ArithmeticResponse add(@WebParam(name = "Add") AddRequest request) throws ApiValidationException;
}
