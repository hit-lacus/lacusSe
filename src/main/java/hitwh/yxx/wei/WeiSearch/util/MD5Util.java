package hitwh.yxx.wei.WeiSearch.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

      public static String string2MD5(String inStr) {
          return DigestUtils.md5Hex(inStr);
      }
}
