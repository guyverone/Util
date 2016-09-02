package com.shd.util.json;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

/**
 * Created by suhd on 2016-09-01.
 */
public class TimestampMorpherTest {
    private static  String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
    private static  String datePattern = "dd/MM/yyyy" ;
    private static  String wrongDatePattern = "yyyy-dd-ll";

    private TimestampMorpher dateTimePatternMorpher;
    private TimestampMorpher wrongDatePatternMorpher;
    private TimestampMorpher datePatternMorpher;

    @Before
    public void init() {
        this.dateTimePatternMorpher = new TimestampMorpher(new String[]{dateTimePattern});
        this.datePatternMorpher = new TimestampMorpher(new String[]{datePattern});
        this.wrongDatePatternMorpher = new TimestampMorpher(new String[]{wrongDatePattern});
    }

    @Test
    public void testMorphOfStringType() {
        String time = "2016-09-01 17:59:10";
        Object obj = this.dateTimePatternMorpher.morph(time);
        Assert.assertTrue(obj instanceof Timestamp);
    }

    @Test
    public void testMorphOfTimestampType() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Object obj = this.dateTimePatternMorpher.morph(time);
        Assert.assertTrue(obj instanceof Timestamp);
    }

    @Test
    public void testMorphOfWrongParameterType() {
        boolean flag = false;
        long time = System.currentTimeMillis();
        try {
            Object obj = this.dateTimePatternMorpher.morph(time);
            flag = true;
        }catch (Exception e) {
            flag = false;
        }
        Assert.assertEquals(false, flag);
    }

    @Test
    public void testMorphOfDatePatternType() {
        String time = "01/09/2016 17:59:10";
        Object obj = this.datePatternMorpher.morph(time);
        Assert.assertTrue(obj instanceof Timestamp);
    }

    @Test
    public void testMorphOfDatePatternTypeWithWrongParameter() {
        String time = "]]01/09/2016 17:59:10";
        Object obj = this.datePatternMorpher.morph(time);
        Assert.assertEquals(null, obj);
    }


    @Test
    public void testMorphOfWrongDateStyle() {
        boolean flag = false;
        String time = "2016-09-01 17:59:10";
        try {
            Object obj = this.wrongDatePatternMorpher.morph(time);
            flag = true;
        }catch (Exception e) {
            flag = false;
        }
        Assert.assertEquals(false, flag);
    }
}
