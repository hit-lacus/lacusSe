package hitwh.yxx.wei.WeiSearch;

import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * JUC指的是Java同步工具/集合
 * 
 * @author  lacus
 * 
 *
 */
public class JucTest {
    private ConcurrentSkipListSet<Integer> csls;
    private TreeSet<Integer> ts;
    private Random ran;
    private static Logger log = Logger.getLogger("JucTest.java");
    
    @Before
    public void initialize(){
        csls = new ConcurrentSkipListSet<Integer>();
        ts = new TreeSet<Integer>();
        ran = new Random(100);  
        for(int i = 1; i <= 100; i++){
            csls.add(i);
            ts.add(i);
        }
    }
    /**
     * 测试ConcurrentSkipListSet在多线程访问下是否安全
     */
    @Test
    public void testConcurrentSkipListSet(){
        log.info("Test Starting!");
        for(int i = 0; i < 6; i++ ){
            //new Thread(new Producer(),"Pro -" + i).start();  
            new Thread(new Cosumer(),"Cos -" + i).start();  
        }
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("RESULT -> count of set " + csls.size());
        
    }
    /**
     * 测试TreeSet 在多线程访问下是否能安全
     */
    @Test
    public void testTreeSet(){
        log.info("Test Starting!");
        for(int i = 0; i < 9; i++ ){
            //new Thread(new Producer(),"Pro -" + i).start();  
            new Thread(new Cosumer1(),"Cos -" + i).start();  
        }
        
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("RESULT -> count of set " + ts.size());
        
    }
    
    
    class Producer implements Runnable{

        @Override
        public void run() {
            while(csls.size() < 20){
                int temp = ran.nextInt(100);
                csls.add(temp);
                log.info(Thread.currentThread().getName() + " add <" + temp + "> to SET.");
                
            }
        }
    }
    
      
    class Cosumer implements Runnable{

        @Override
        public void run() {
            while(!csls.isEmpty()){
                
                int temp = csls.pollFirst();
                log.info(Thread.currentThread().getName() + " remove <" + temp + "> to SET.");
                try {
                    Thread.sleep(Math.abs(ran.nextLong()%1000));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    class Cosumer1 implements Runnable{

        @Override
        public void run() {
            while(!ts.isEmpty()){
                
                int temp = ts.pollFirst();
                log.info(Thread.currentThread().getName() + " remove <" + temp + "> to SET.");
                try {
                    Thread.sleep(Math.abs(ran.nextLong()%1000 + 200));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    

}
