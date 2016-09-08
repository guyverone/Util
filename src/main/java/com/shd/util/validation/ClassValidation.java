package com.shd.util.validation;


import com.shd.exception.ClassValidationException;
import common.domain.annotation.Greater;
import common.domain.annotation.GreaterOrEqual;
import common.domain.annotation.Label;
import common.domain.annotation.NotEmpty;
import common.domain.finance.EnumUsedBusinessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * <p>Validation for validating the Class which contains property type of UsedBusinessType。</p>
 *
 * <br>Support validating of NotEmpty,Greater,GreaterOrEqual, more validating type is coming in.</br>
 *
 * <p>Created by suhd on 2016-07-29.</p>
 *
 */
public class ClassValidation {

    private static Logger logger = LoggerFactory.getLogger(ClassValidation.class);

    private ClassValidation() {}

    /**
     * @param targetObj
     * @throws Exception
     */
    public static void validate(Object targetObj) throws ClassValidationException {
        new ClassValidation().doValidate(targetObj);
    }

    private void doValidate(Object targetObj) throws ClassValidationException {
        Field[] targetFields = getFields(targetObj);
        EnumUsedBusinessType usedBusinessType = getUsedBusinessType(targetFields, targetObj);
        /**
         * validation support for validating Collection which inside one Class.  added 2016-9-3
         */
        for(int i=0; i<targetFields.length; i++) {
            Object retObj = getFieldValue(targetFields[i], targetObj);
            if(retObj instanceof Collection) {
                Collection c = (Collection)retObj;
                Iterator it = c.iterator();
                while(it.hasNext()) {
                    this.doValidate(it.next());
                }
            }else {
                validateAtomicAnnotationField(targetFields[i], targetObj, usedBusinessType);
            }
        }
    }

    private Field[] getFields(Object targetObj) {
        return targetObj.getClass().getDeclaredFields();
    }

    private EnumUsedBusinessType getUsedBusinessType(Field[] targetFields, Object targetObj) throws ClassValidationException {
        for(int i=0; i<targetFields.length; i++) {
            if("usedBusinessType".equals(targetFields[i].getName()) && getFieldValue(targetFields[i], targetObj) != null && !"".equals(getFieldValue(targetFields[i], targetObj))) {
                return (EnumUsedBusinessType)getFieldValue(targetFields[i], targetObj);
            }
        }
        return null;
    }

    private Object getFieldValue(Field targetField, Object targetObj) throws ClassValidationException {
        try {
            targetField.setAccessible(true);
            return targetField.get(targetObj);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()), e);
            return new ClassValidationException("getFieldValueError, please try it later.");
        }
    }

    private void validateAtomicAnnotationField(Field targetField, Object targetObj, EnumUsedBusinessType usedBusinessType) throws ClassValidationException {
        Annotation[] targetAnnotations = getAnnotations(targetField);
        for(int j=0; j<targetAnnotations.length; j++) {
            checkNotEmpty(usedBusinessType, targetField, targetAnnotations[j], targetObj);
            checkGreater(targetField, targetAnnotations[j], targetObj);
            checkGreaterOrEqual(targetField, targetAnnotations[j], targetObj);
        }
    }

    private Annotation[] getAnnotations(Field targetField) {
        return targetField.getAnnotations();
    }

    private void checkNotEmpty(EnumUsedBusinessType usedBusinessType, Field targetField, Annotation targetAnnotation, Object targetObj) throws ClassValidationException {
        if(targetAnnotation != null && targetAnnotation instanceof NotEmpty) {
            this.doCheckNotEmpty(usedBusinessType, targetField, targetObj);
        }
    }

    private void doCheckNotEmpty(EnumUsedBusinessType usedBusinessType, Field targetField, Object targetObj) throws ClassValidationException {
        EnumUsedBusinessType[] value = targetField.getAnnotation(NotEmpty.class).value();
        EnumUsedBusinessType[] exclusion = targetField.getAnnotation(NotEmpty.class).exclusion();

        boolean valueIsNull = isEnumUsedBusinessTypeEmpty(value);
        boolean exclusionIsNull = isEnumUsedBusinessTypeEmpty(exclusion);

        //if the EnumUsedBusinessType type is Not specific(EnumUsedBusinessType is empty), then validates it.
        if(!doValidateField(valueIsNull, exclusionIsNull, usedBusinessType, targetField, targetObj)) {
            return;
        }
        //if there's a exclusion, then exit.
        if(isExclusion(exclusion, usedBusinessType)) {
            return;
        }
        //validating the type of not specific
        if(doValidateNoneSpecificField(valueIsNull, value, usedBusinessType)) {
            return;
        }
        //validating the specific type
        if(doValidateSpecificField(valueIsNull, value, usedBusinessType, targetField, targetObj)) {
            return;
        }
        //validating <such as：@NotEmpty(exclusion=EnumUsedBusinessType.SALE)> the none exclude type.
        doValidateNoneExcludedField(exclusionIsNull, exclusion, usedBusinessType, targetField, targetObj);
    }

    private boolean isEnumUsedBusinessTypeEmpty(EnumUsedBusinessType[] targetValue) {
        return targetValue.length==1 && EnumUsedBusinessType.EMPTY.getType().equals(targetValue[0].getType());
    }

    private boolean doValidateField(boolean valueIsNull, boolean exclusionIsNull, EnumUsedBusinessType usedBusinessType, Field targetField, Object targetObj) throws ClassValidationException {

        /**
         * when <such as：@NotEmpty> value is Null and exclusion is Null，then do validate all of types.
         * case 1: value[] has only one default value of empty and exclusion[] has only one default value of empty either, that means there's no specific EnumUsedBusinessType type.
         */
        if(valueIsNull && exclusionIsNull) {
            checkFieldValueNullOrEmpty(targetField, targetObj);
            return false;
        }
        /**
         * when <such as：@NotEmpty> not specify the EnumUsedBusinessType type and usedBusinessType is null，then do validate all of types.
         * case 2: if the value of usedBusinessType is null and @NotEmpty does not specify a specific TYPE means that do validate all of types.
         *
         * when <such as：@NotEmpty> specify the EnumUsedBusinessType type and usedBusinessType is null，then do validate the specific types.
         * case 3: if the value of usedBusinessType is null and @NotEmpty does specify a specific TYPE means that only that specific type be validating.
         */
        if(usedBusinessType==null) {
            //case 2:
            if(valueIsNull) {
                checkFieldValueNullOrEmpty(targetField, targetObj);
                return false;
            }
            //case 3:
            else {
                return false;
            }
        }

        return true;
    }

    /**
     * when <such as：@NotEmpty(exclusion=EnumUsedBusinessType.SALE)> exclusion value is set, then that type(EnumUsedBusinessType.SALE) won't be to do validating. caution: when conflict appear, exclusion will do the charge.
     */
    private boolean isExclusion(EnumUsedBusinessType[] exclusion, EnumUsedBusinessType usedBusinessType) throws ClassValidationException {
        for(int i=0; i<exclusion.length; i++) {
            if(exclusion[i].getType().equals(usedBusinessType.getType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * validating <such as：@NotEmpty(EnumUsedBusinessType.SALE)> the specific EnumUsedBusinessType type.
     */
    private boolean doValidateSpecificField(boolean valueIsNull, EnumUsedBusinessType[] value, EnumUsedBusinessType usedBusinessType, Field targetField, Object targetObj) throws ClassValidationException {
        for(int i=0; i<value.length; i++) {
            if(!valueIsNull && value[i].getType().equals(usedBusinessType.getType())) {
                checkFieldValueNullOrEmpty(targetField, targetObj);
                return true;
            }
        }
        return false;
    }

    /**
     * validating <such as：@NotEmpty(EnumUsedBusinessType.SALE> the not specific EnumUsedBusinessType type.
     */
    private boolean doValidateNoneSpecificField(boolean valueIsNull, EnumUsedBusinessType[] value, EnumUsedBusinessType usedBusinessType) throws ClassValidationException {
        for(int i=0; i<value.length; i++) {
            if(!valueIsNull && !value[i].getType().equals(usedBusinessType.getType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * validating <such as：@NotEmpty(exclusion=EnumUsedBusinessType.SALE)> the none exclude type.
     */
    private void doValidateNoneExcludedField(boolean exclusionIsNull, EnumUsedBusinessType[] exclusion, EnumUsedBusinessType usedBusinessType, Field targetField, Object targetObj) throws ClassValidationException {
        for(int i=0; i<exclusion.length; i++) {
            if(!exclusionIsNull && !exclusion[i].getType().equals(usedBusinessType.getType())) {
                checkFieldValueNullOrEmpty(targetField, targetObj);
                return;
            }
        }
    }

    private void checkFieldValueNullOrEmpty(Field targetField, Object targetObj) throws ClassValidationException {
        Object verifyObj = getFieldValue(targetField, targetObj);
        if (verifyObj == null || "".equals(verifyObj.toString())) {
            throw new ClassValidationException(getFieldLabelValue(targetField));
        }
    }

    private String getFieldLabelValue(Field targetField) {
        Label label = targetField.getAnnotation(Label.class);
        if(label != null && !"".equals(label.value())) {
            return label.value();
        }
        return targetField.getName();
    }

    private void checkGreater(Field targetField, Annotation targetAnnotation, Object targetObj) throws ClassValidationException {
        if(targetAnnotation != null && targetAnnotation instanceof Greater) {
            Object verifyObj = getFieldValue(targetField, targetObj);
            isBiggerThanSpecificValue(verifyObj, targetField);
        }
    }

    private void isBiggerThanSpecificValue(Object verifyObj, Field targetField) throws ClassValidationException {
        if(verifyObj != null && verifyObj instanceof Number) {
            Number fieldNumber = (Number) verifyObj;
            Number annotationNumber = (Number)targetField.getAnnotation(Greater.class).value();
            if(fieldNumber.doubleValue() <= annotationNumber.doubleValue()) {
                throw new ClassValidationException(getFieldLabelValue(targetField) + " must bigger than " + annotationNumber);
            }
        }
    }

    private void checkGreaterOrEqual(Field targetField, Annotation targetAnnotation, Object targetObj) throws ClassValidationException {
        if(targetAnnotation != null && targetAnnotation instanceof GreaterOrEqual) {
            Object verifyObj = getFieldValue(targetField, targetObj);
            isGreaterOrEqualSpecificValue(verifyObj, targetField);
        }
    }

    private void isGreaterOrEqualSpecificValue(Object verifyObj, Field targetField) throws ClassValidationException {
        if(verifyObj != null && verifyObj instanceof Number) {
            Number fieldNumber = (Number) verifyObj;
            Number annotationNumber = (Number)targetField.getAnnotation(GreaterOrEqual.class).value();
            if(fieldNumber.doubleValue() < annotationNumber.doubleValue()) {
                throw new ClassValidationException(getFieldLabelValue(targetField) + " must bigger than or be equal of " + annotationNumber);
            }
        }
    }
}
