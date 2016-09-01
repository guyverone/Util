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
        for(int i=0; i<targetFields.length; i++) {
            Annotation[] targetAnnotations = getAnnotations(targetFields[i]);
            for(int j=0; j<targetAnnotations.length; j++) {
                checkNotEmpty(usedBusinessType, targetFields[i], targetAnnotations[j], targetObj);
                checkGreater(targetFields[i], targetAnnotations[j], targetObj);
                checkGreaterOrEqual(targetFields[i], targetAnnotations[j], targetObj);
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
        throw new ClassValidationException("Validation for validating the Class which contains property type of UsedBusinessType");
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

    private Annotation[] getAnnotations(Field targetField) {
        return targetField.getAnnotations();
    }

    private boolean checkNotEmpty(EnumUsedBusinessType usedBusinessType, Field targetField, Annotation targetAnnotation, Object targetObj) throws ClassValidationException {
        if(targetAnnotation != null && targetAnnotation instanceof NotEmpty) {
            //no specify parameter, then the value must be not empty.
            if(EnumUsedBusinessType.EMPTY.getType().equals((targetField.getAnnotation(NotEmpty.class).value()[0]).getType())) {
                checkFieldValueNullOrEmpty(targetField, targetObj);
                return false;
            }
            //only the specifies type of EnumUsedBusinessType of Master will be validating.
            if(usedBusinessType.getType().equals((targetField.getAnnotation(NotEmpty.class).value()[0]).getType())) {
                checkFieldValueNullOrEmpty(targetField, targetObj);
                return false;
            }
        }
        return true;
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
