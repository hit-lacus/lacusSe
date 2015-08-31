package hitwh.yxx.wei.WeiSearch.util;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class MD5UtilTest {
    private static Logger log = Logger.getLogger("MD5UtilTest.java");
    private String key;
    private String value;
    
    
    @Before
    public void initialize() throws Exception{
          key = "哈工大威海俞霄翔";
          value = "c4a5bc5c63e07c69e777eb4b763b746b";
    }

    @Test
    public void testMd5Util(){
        String mD5 = MD5Util.string2MD5(key);
        Assert.assertEquals(mD5, value);
    }

}
