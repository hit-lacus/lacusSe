package hitwh.yxx.wei.WeiSearch.bean;

import hitwh.yxx.wei.WeiSearch.constant.Constant;

import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.log4j.Logger;

public class UrlPool {
    
    
    private static Logger log = Logger.getLogger("UrlPool.java");
    private ConcurrentSkipListSet<String> visitedUrl;
    private ConcurrentSkipListSet<String> newUrl;
    private ConcurrentSkipListSet<Page> pages;
    private boolean couldAdd;
    
    
    public UrlPool(){
        visitedUrl = new ConcurrentSkipListSet<String> ();
        newUrl = new ConcurrentSkipListSet<String> ();
        pages = new ConcurrentSkipListSet<Page>();
        
        //TODO
        couldAdd = true;
    }

    /**
     * 向链接池获取链接
     * @param url
     * @return
     */
    public boolean addUrl(String url) {
        
        if(visitedUrl.contains(url)){
            log.info("无法添加：" + url + "，该链接已存在。");
            return false;
        }
        
        if(newUrl.size() >= Constant.MAX_NEW_URL){
            couldAdd = false;
        }else{
            couldAdd = true;
        }
        
        if(couldAdd){
            //log.info("可以添加：" + url + "，everything allright!");
            newUrl.add(url);    
        }else{
            log.info("暂时无法添加：" + url + "，链接池已满!");
            try {
                Thread.sleep(1000);
                return false;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
        
    }

    /**
     * 从链接池获取链接
     * @return
     */
    public String getUrl() {
        if(newUrl.size() <= 0){
            log.info("链接池中竟然不存在链接，太不可思议了!");
            try {
                Thread.sleep(1000);
                return null;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
        
        /**
         * 假如visitedUrl里包含的链接过多，则清空
         */
        if(visitedUrl.size() > Constant.MAX_VISITED_URL){
            visitedUrl.clear();
        }
        
        String url = newUrl.pollFirst();
        visitedUrl.add(url);  
        return url;
    }
    
    /**
     * 将整理好的数据暂时保存在内存<br>
     * 出于安全考虑，暂将此方法设置为同步方法，因为这是三个方法调用是一个原子操作
     * @param page
     * @param url
     * @return
     */
    public synchronized boolean addPage(Page page,String url){
        if(visitedUrl.contains(url)){
            log.info("该链接已经存在！");
            return false;
        }
        
        if(pages.size() > Constant.MAX_PAGES){
            
        }
        
        log.info("正在向内存添加信息：" + page.getUrl());
        pages.add(page);
        newUrl.remove(url);
        visitedUrl.add(url);
        return true;
    }
    
    /**
     * 向数据库写入记录
     */
    public synchronized void storePages(){
        
    }
    
    
    

    public ConcurrentSkipListSet<String> getVisitedUrl() {
        return visitedUrl;
    }

    public void setVisitedUrl(ConcurrentSkipListSet<String> visitedUrl) {
        this.visitedUrl = visitedUrl;
    }

    public ConcurrentSkipListSet<String> getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(ConcurrentSkipListSet<String> newUrl) {
        this.newUrl = newUrl;
    }

    public ConcurrentSkipListSet<Page> getPages() {
        return pages;
    }

    public void setPages(ConcurrentSkipListSet<Page> pages) {
        this.pages = pages;
    }

    public boolean isCouldAdd() {
        return couldAdd;
    }

    public void setCouldAdd(boolean couldAdd) {
        this.couldAdd = couldAdd;
    }

}
