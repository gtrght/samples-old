package com.vvlasov.samples.jaxws.api.v1.soap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * User: Vasily Vlasov
 * Date: 22.04.13
 */
@XmlType
public class ArithmeticResponse {
    private Integer result;

    public ArithmeticResponse() {
    }

    public ArithmeticResponse(Integer result) {
        this.result = result;
    }

    @XmlElement(name = "Result")
    //@XmlValue also can be used. If a class has @XmlElement property, it cannot have @XmlValue property.
    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
