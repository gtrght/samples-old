package com.vvlasov.samples.jaxws.api.v1.soap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * User: Vasily Vlasov
 * Date: 22.04.13
 */
@XmlType (propOrder = {"op1", "op2"})
public class AddRequest {
    private Integer op2;
    private Integer op1;

    @XmlAttribute(name = "operand2")
    public Integer getOp2() {
        return op2;
    }

    public void setOp2(Integer op2) {
        this.op2 = op2;
    }

    @XmlAttribute(name = "operand1")
    public Integer getOp1() {
        return op1;
    }

    public void setOp1(Integer op1) {
        this.op1 = op1;
    }
}
