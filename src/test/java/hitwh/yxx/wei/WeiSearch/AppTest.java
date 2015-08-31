package hitwh.yxx.wei.WeiSearch;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
      private static Logger log = Logger.getLogger("hitwh.yxx.wei.WeiSearch.AppTest");

      @Test
      public void printJavaVersion() {
            
            System.out.println(System.getProperty("java.version"));
            Map<Object,Object> ps = System.getProperties();
            for(Object o : ps.keySet()){
                  log.info(o + " : " + System.getProperty(o.toString()));
            }
            

      }

}
