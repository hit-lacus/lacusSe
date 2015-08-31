package hitwh.yxx.wei.WeiSearch;

import hitwh.yxx.wei.WeiSearch.bean.UrlPool;
import hitwh.yxx.wei.WeiSearch.util.Spy;

public class App {
    
    private static UrlPool pool;
    
    public static void main(String[] args) {
        pool = new UrlPool();
        pool.addUrl("http://www.sina.com");
        pool.addUrl("http://www.baidu.com");
        pool.addUrl("http://www.163.com");
        pool.addUrl("http://www.qq.com");
        pool.addUrl("http://www.1688.com");
        pool.addUrl("http://www.cctv.com");
        pool.addUrl("http://www.csdn.net");
        pool.addUrl("http://www.huawei.com");
 
        new Thread(new Spy(pool)," SPY - 1").start();
        new Thread(new Spy(pool)," SPY - 2").start();
        new Thread(new Spy(pool)," SPY - 3").start();
        new Thread(new Spy(pool)," SPY - 4").start();
        new Thread(new Spy(pool)," SPY - 5").start();
        new Thread(new Spy(pool)," SPY - 6").start();
        new Thread(new Spy(pool)," SPY - 7").start();
        new Thread(new Spy(pool)," SPY - 8").start();
    }
}
