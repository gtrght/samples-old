package com.vvlasov.samples.jaxws.api.v1;

import com.vvlasov.samples.jaxws.api.v1.exception.ApiValidationException;
import com.vvlasov.samples.jaxws.api.v1.soap.AddRequest;
import com.vvlasov.samples.jaxws.api.v1.soap.ArithmeticResponse;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * User: Vasily Vlasov
 * Date: 22.04.13
 */

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cxf-api-beans.xml"})
public class ArithmeticServiceTest {

    @Autowired
    private ArithmeticService arithmeticService;

    @Test
    public void testAdd() throws Exception {
        AddRequest request = new AddRequest();
        request.setOp1(1);
        request.setOp2(2);
        ArithmeticResponse response = arithmeticService.add(request);
        assertThat(response.getResult(), equalTo(3));
    }

    @Test (expected = ApiValidationException.class)
    public void testAddNullOp1() throws Exception {
        AddRequest request = new AddRequest();
        request.setOp2(1);
        arithmeticService.add(request);
    }

    @Test (expected = ApiValidationException.class)
    public void testAddNullOp2() throws Exception {
        AddRequest request = new AddRequest();
        request.setOp1(1);
        arithmeticService.add(request);
    }
}
