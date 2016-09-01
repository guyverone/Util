package com.shd.util.validation;


import common.domain.finance.EnumUsedBusinessType;
import common.domain.finance.Master;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by suhd on 2016-07-30.
 */
public class ClassValidationTest {

    @Test
    public void testValidate() throws Exception  {
        boolean flag = false;
        Master fm = new Master();

        fm.setCompanyId("null");
        fm.setCompanyName("so.getCrmName()");
        fm.setSubCmpId("so.getShopId()");
        fm.setSubCmpName("so.getShopName()");
        fm.setTtlamt(new BigDecimal(0.00001));
        fm.setTaxRate(new BigDecimal(0));

        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEA);

        try{
            ClassValidation.validate(fm);
            flag = true;
        }catch (Exception e) {
            flag = false;
        }

        Assert.assertEquals(true, flag);

    }
}
