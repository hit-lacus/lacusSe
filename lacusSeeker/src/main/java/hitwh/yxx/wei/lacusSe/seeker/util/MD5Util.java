package hitwh.yxx.wei.lacusSe.seeker.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

      public static String string2MD5(String inStr) {
    	  if(inStr == null) return "";
          return DigestUtils.md5Hex(inStr);
      }
}
