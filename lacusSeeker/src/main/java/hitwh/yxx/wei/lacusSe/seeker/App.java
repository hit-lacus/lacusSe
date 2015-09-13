package hitwh.yxx.wei.lacusSe.seeker;

import org.apache.log4j.Logger;
import hitwh.yxx.wei.lacusSe.seeker.bean.UrlPool;
import hitwh.yxx.wei.lacusSe.seeker.util.Spy;


/**
 * 爬虫启动入口
 * @author lacus
 */
public class App {
    
    private static UrlPool pool;
    private static Logger log = Logger.getLogger("App.java"); 
    
    /**
     * 
     * @param args 配置文件地址
     */
    public static void main(String[] args) {
    	
        pool = new UrlPool();
        /*
    	if(args.length > 0){
    		System.out.println("您输入了·：");
    		for(String s : args){
    			System.out.println(s);
    		}
    	}else{
    		pool.addUrl("http://baike.haosou.com");//默认爬取百度百科的网页
    	}*/
    	pool.addUrl("http://baike.baidu.com");
        
    	log.info(" LacusSe Started ");
 
        new Thread(new Spy(pool,Spy.HTML_PARSER)," SPY - 1").start();
        new Thread(new Spy(pool,Spy.HTML_PARSER)," SPY - 2").start();
        new Thread(new Spy(pool,Spy.URL_COLLECTOR)," SPY - 3").start();
        new Thread(new Spy(pool,Spy.URL_COLLECTOR)," SPY - 4").start();
        new Thread(new Spy(pool,Spy.URL_COLLECTOR)," SPY - 5").start();
    }
}
