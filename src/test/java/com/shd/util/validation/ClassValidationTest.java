package com.shd.util.validation;


import com.shd.exception.ClassValidationException;
import common.domain.finance.EnumUsedBusinessType;
import common.domain.finance.Master;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by suhd on 2016-07-30.
 */
public class ClassValidationTest {

    private Logger logger = LoggerFactory.getLogger(ClassValidationTest.class);

    private Master fm = new Master();

    private boolean returnFlag(Master fm) {
        try{
            ClassValidation.validate(fm);
            return true;
        }catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()), e);
            return false;
        }
    }

    @Before
    public void init() {
        fm.setCompanyId("companyId");
        fm.setCompanyName("companyName");
        fm.setSubCmpId("subCmpId");
        fm.setSubCmpName("subCmpName");
        fm.setTtlamt(new BigDecimal(0.00001));
        fm.setTaxRate(new BigDecimal(0));
    }

    @Test
    /**
     * subCmpName is allowed to be empty in a field which does not use annotation @NotEmpty
     * with value, return true.
     */
    public void testValidateTYPEAOfDoNotUseNotEmptyWithValue() {
        fm.setSubCmpName("subCmpName");
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEA);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * subCmpName is allowed to be empty in a field which does not use annotation @NotEmpty
     * without value, return true.
     */
    public void testValidateTYPEAOfDoNotUseNotEmptyWithoutValue() {
        fm.setSubCmpName(null);
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEA);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * companyName does not allowed to be empty in a field which used annotation @NotEmpty
     * with value, return true.
     */
    public void testValidateTYPEAOfNotEmptyWithValue() {
        fm.setCompanyName("companyName");
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEA);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * companyName does not allowed to be empty in a field which used annotation @NotEmpty
     * without value, return false.
     */
    public void testValidateTYPEAOfNotEmptyWithoutValue() {
        fm.setCompanyName(null);
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEA);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(false, flag);
    }

    @Test
    /**
     * companyId does not allowed to be empty in a field which specifies TYPEA can't be empty
     * with value, return true.
     */
    public void testValidateTYPEAOfNotEmptyTYPEAWithValue() {
        fm.setCompanyId("companyId");
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEA);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * companyId does not allowed to be empty in a field which specifies TYPEA can't be empty
     * without value, return false.
     */
    public void testValidateTYPEAOfNotEmptyTYPEAWithoutValue() {
        fm.setCompanyId(null);
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEA);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(false, flag);
    }

    @Test
    /**
     * subCmpId does not allowed to be empty in a field which only exclude TYPEB that can be empty
     * with value, return true.
     */
    public void testValidateTYPEAOfNotEmptyExclusionTYPEBWithValue() {
        fm.setSubCmpId("subCmpId");
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEA);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * subCmpId does not allowed to be empty in a field which only exclude TYPEB that can be empty
     * without value, return false.
     */
    public void testValidateTYPEAOfNotEmptyExclusionTYPEBWithoutValue() {
        fm.setSubCmpId(null);
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEA);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(false, flag);
    }

    @Test
    /**
     * companyId is allowed to be empty in a field which only validating TYPEA of not being empty
     * with value, return true.
     */
    public void testValidateTYPEBOfNotEmptyTYPEAWithValue() {
        fm.setCompanyId("companyId");
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEB);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * companyId is allowed to be empty in a field which only validating TYPEA of not being empty
     * without value, return true.
     */
    public void testValidateTYPEBOfNotEmptyTYPEAWithoutValue() {
        fm.setCompanyId(null);
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEB);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * companyName does not allowed to be empty in a field which used annotation @NotEmpty
     * with value, return true.
     */
    public void testValidateTYPEBOfNotEmptyWithValue() {
        fm.setCompanyName("companyName");
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEB);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * companyName does not allowed to be empty in a field which used annotation @NotEmpty
     * without value, return true.
     */
    public void testValidateTYPEBOfNotEmptyWithoutValue() {
        fm.setCompanyName(null);
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEB);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(false, flag);
    }

    @Test
    /**
     * subCmpId does not allowed to be empty in a field which only exclude TYPEB that can be empty
     * with value, return true.
     */
    public void testValidateTYPEBOfNotEmptyExclusionTYPEBWithValue() {
        fm.setSubCmpId("subCmpId");
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEB);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * subCmpId does not allowed to be empty in a field which only exclude TYPEB that can be empty
     * without value, return true.
     */
    public void testValidateTYPEBOfNotEmptyExclusionTYPEBWithoutValue() {
        fm.setSubCmpId(null);
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEB);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * subCmpName is allowed to be empty in a field which does not use annotation @NotEmpty
     * with value, return true.
     */
    public void testValidateTYPEBOfDoNotUseNotEmptyWithValue() {
        fm.setSubCmpName("subCmpName");
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEB);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * subCmpName is allowed to be empty in a field which does not use annotation @NotEmpty
     * without value, return true.
     */
    public void testValidateTYPEBOfDoNotUseNotEmptyWithoutValue() {
        fm.setSubCmpName(null);
        fm.setUsedBusinessType(EnumUsedBusinessType.TYPEB);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * companyId can be empty in NotSpecificType
     * with value, return true.
     */
    public void testValidateNotSpecificTypeOfNotEmptyInTYPEAWithValue() {
        fm.setCompanyId("companyId");
        fm.setUsedBusinessType(null);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * companyId can be empty in NotSpecificType
     * without value, return true.
     */
    public void testValidateNotSpecificTypeOfNotEmptyInTYPEAWithoutValue() {
        fm.setCompanyId(null);
        fm.setUsedBusinessType(null);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * companyName does not allowed to be empty in a field which used annotation @NotEmpty
     * with value, return true.
     */
    public void testValidateNotSpecificTypeOfNotEmptyWithValue() {
        fm.setCompanyName("companyName");
        fm.setUsedBusinessType(null);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * companyName does not allowed to be empty in a field which used annotation @NotEmpty
     * without value, return true.
     */
    public void testValidateNotSpecificTypeOfNotEmptyWithoutValue() {
        fm.setCompanyName(null);
        fm.setUsedBusinessType(null);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(false, flag);
    }

    @Test
    /**
     * subCmpId does not allowed to be empty in a field which only exclude TYPEB that can be empty
     * with value, return true.
     */
    public void testValidateNotSpecificTypeOfNotEmptyExclusionTYPEBWithValue() {
        fm.setSubCmpId("subCmpId");
        fm.setUsedBusinessType(null);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * subCmpId does not allowed to be empty in a field which only exclude TYPEB that can be empty
     * without value, return false.
     */
    public void testValidateNotSpecificTypeOfNotEmptyExclusionTYPEBWithoutValue() {
        fm.setSubCmpId(null);
        fm.setUsedBusinessType(null);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(false, flag);
    }

    @Test
    /**
     * subCmpName is allowed to be empty in a field which does not use annotation @NotEmpty
     * with value, return true.
     */
    public void testValidateNotSpecificTypeOfDoNotUseNotEmptyWithValue() {
        fm.setSubCmpName("subCmpName");
        fm.setUsedBusinessType(null);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * subCmpName is allowed to be empty in a field which does not use annotation @NotEmpty
     * without value, return true.
     */
    public void testValidateNotSpecificTypeOfDoNotUseNotEmptyWithoutValue() {
        fm.setSubCmpName(null);
        fm.setUsedBusinessType(null);
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * ttlamt is need to be greater than zero with annotation @Greater
     * input value of 1, return true.
     */
    public void testValidateGreaterWithValueOne() {
        fm.setTtlamt(new BigDecimal(1));
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * ttlamt is need to be greater than zero with annotation @Greater
     * input value of 0, return false.
     */
    public void testValidateGreaterWithValueZero() {
        fm.setTtlamt(new BigDecimal(0));
        boolean flag = returnFlag(fm);
        Assert.assertEquals(false, flag);
    }

    @Test
    /**
     * ttlamt is need to be greater than zero with annotation @Greater
     * input value of -1, return false.
     */
    public void testValidateGreaterWithValueNegativeOne() {
        fm.setTtlamt(new BigDecimal(-1));
        boolean flag = returnFlag(fm);
        Assert.assertEquals(false, flag);
    }

    @Test
    /**
     * taxRate is need to be greater than zero with annotation @Greater
     * input value of 1, return true.
     */
    public void testValidateGreaterOrEqualWithValueOne() {
        fm.setTaxRate(new BigDecimal(1));
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * taxRate is need to be greater than zero with annotation @Greater
     * input value of 0, return false.
     */
    public void testValidateGreaterOrEqualWithValueZero() {
        fm.setTaxRate(new BigDecimal(0));
        boolean flag = returnFlag(fm);
        Assert.assertEquals(true, flag);
    }

    @Test
    /**
     * taxRate is need to be greater than zero with annotation @Greater
     * input value of -1, return false.
     */
    public void testValidateGreaterOrEqualWithValueNegativeOne() {
        fm.setTaxRate(new BigDecimal(-1));
        boolean flag = returnFlag(fm);
        Assert.assertEquals(false, flag);
    }

    @Test(expected = ClassValidationException.class)
    /**
     * taxRate is need to be greater than zero with annotation @Greater
     * input value of -1, throw exception of ClassValidationException.
     */
    public void testValidateGreaterOrEqualWithValueNegativeOneExpectedExceptionVersion() throws Exception{
        fm.setTaxRate(new BigDecimal(-1));
        ClassValidation.validate(fm);
    }
}
