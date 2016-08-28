package com.shd.util.validation;


import common.domain.finance.EnumUsedBusinessType;
import common.domain.finance.Master;

import java.math.BigDecimal;

/**
 * Created by suhd on 2016-07-30.
 */
public class TypeClassValidationTest {

    public static void main(String[] args) throws Exception  {
        new TypeClassValidationTest().testValidateFinComMaster();
    }

    private void testValidateFinComMaster() throws Exception  {
        Master fm = new Master();

        fm.setCompanyId("null");
        fm.setCompanyName("so.getCrmName()");
        fm.setSubCmpId("so.getShopId()");
        fm.setSubCmpName("so.getShopName()");
        fm.setTtlamt(new BigDecimal(0.00001));
        fm.setTaxRate(new BigDecimal(0));

        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEA);

        try {
            ClassValidation.validate(fm);
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }
}
