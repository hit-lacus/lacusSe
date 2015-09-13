package hitwh.yxx.wei.lacusSe.seeker.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.log4j.Logger;

import com.mysql.jdbc.Blob;

import hitwh.yxx.wei.lacusSe.seeker.constant.Constant;

/**
 * 链接池，用于管理、保存、持久化链接和网页详细信息
 * @author lacus
 *
 */
public class UrlPool {
    /**已访问的链接*/
    private ConcurrentSkipListSet<String> visitedUrl;
    /**刚抓取的链接*/
    private ConcurrentSkipListSet<String> newUrl;
    /**获取的网页详细信息*/
    private ConcurrentSkipListSet<Page> pages;
    /**标志位，标志新链接是否超额*/
    private boolean couldAdd;
    private static Logger log = Logger.getLogger("UrlPool.java");
    
    public UrlPool(){
        visitedUrl = new ConcurrentSkipListSet<String> ();
        newUrl = new ConcurrentSkipListSet<String> ();
        pages = new ConcurrentSkipListSet<Page>();
        couldAdd = true;
    }

    /**
     * 向链接池添加链接
     * @param url
     * @return 添加成功否
     */
    public boolean addUrl(String url) {
    	/**链接过长，这可能不是一个正常的链接*/
        if(url.length() > 90){
        	log.info("无法添加：" + url + "，链接过长。");
        	return false;
        }
        
        /**如果不是以百度百科url或包含了重定向关键词，则跳出*/
        if(url.startsWith("http://baike.baidu")  || url.startsWith("/")){
        	if(url.startsWith("/")){
        		url = "http://baike.baidu.com" + url;
        	}
        }else{
        	return false;
        }
        
        if(url.contains("redirect")){
        	//log.info("无法添加：" + url + "，重定向链接。");
        	return false;
        }
        
        if(url.endsWith("ipa")){
        	return false;
        }
    	
    	
        /**假如本链接已经被访问过，则返回false
         * */
        if(visitedUrl.contains(url)){
            log.info("无法添加：" + url + "，该链接已存在。");
            return false;
        }
        
        /*假如链接池已满，则给couldAdd置false位
         */
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
            return false;
        }
        return true;
        
    }

    /**
     * 从链接池获取链接
     * @return 获取的链接
     */
    public String getUrl() {     
        /**
         * 假如visitedUrl里包含的链接过多，则清空
         */
        if(visitedUrl.size() >= Constant.MAX_VISITED_URL){
            visitedUrl.clear();
        }
        //Retrieves and removes the first (lowest) element, or returns null if this set is empty.
        String url = newUrl.pollFirst();
        if( url == null){ 
        	log.error("链接池为空");
        }
        return url;
    }
    
    /**
     * 将整理好的数据暂时保存在内存<br>
     * 出于安全考虑，暂将此方法设置为同步方法，因为这是三个方法调用是一个原子操作
     * @param page
     * @param url
     * @return 
     */
    public synchronized boolean addPage(Page page){
        if(visitedUrl.contains(page.getUrl())){
            log.info("该链接已经存在！");
            return false;
        }
        
        /**r认为标题为空的页面没有意义*/
        if(page.getTitle() == null){
        	log.info("无标题的页面！");
        	return false;
        }
        
        /***/
        if(pages.size() >= Constant.MAX_PAGES){	
        	log.debug("触发持久化操作 ");
        	storePages();
        }

        log.info("正在向内存添加信息：" + page.getUrl());
        pages.add(page);
        visitedUrl.add(page.getUrl());
        return true;
    }
    
    /**
     * 向数据库写入记录，采取批量插入的操作方式
     */
    public synchronized boolean storePages(){
    	
    	if(pages.isEmpty())  return true;
    	
    	/**注意配置预编译sql和开启批量操作*/
    	String db = "jdbc:mysql://127.0.0.1:3306/test?useServerPrepStmts=false&rewriteBatchedStatements=true";
		String user = "root";
		String password = "root";
		
		Connection conn = null;
		PreparedStatement psts;
		/* 建表语句
		 * CREATE TABLE `page` (
		  `id` varchar(16) CHARACTER SET latin1 NOT NULL,    -> md5
		  `url` varchar(200),                                -> url
		  `title` varchar(400),                              -> title
		  `kw` varchar(2000),                                -> keyword
		  `desc` varchar(4000),                              -> description
		  `date` timestamp,                                  -> date
		  `text` mediumtext,                                 -> utfContent
		  PRIMARY KEY (`id`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8;
		 *  
		 *  */
		
		/**使用replace而不是insert*/
		String sql = "REPLACE INTO temp VALUES(?,?,?,?,?,?,?)";
		
		try {
			conn = DriverManager.getConnection(db, user, password);
			conn.setAutoCommit(false);
			if(!conn.isClosed()){
				log.info("数据库连接成功!");
				log.info("你连接了 " + conn.getCatalog());
			}else{
				log.info("数据库连接失败!");
				conn.close();
				return false;
			}	
			
			psts = conn.prepareStatement(sql);	
			
			/**批量操作*/
			for(Page p : pages){
				psts.setString(1, p.getMd5());
				psts.setString(2, p.getUrl());
				psts.setString(3, p.getTitle());
				psts.setString(4, p.getKeyword());
				psts.setString(5, p.getDescription());
				psts.setTimestamp(6, new Timestamp(p.getDate().getTime()));
				psts.setString(7,p.getUtfContent() );
				psts.addBatch();
			}
			log.info("正在提交批量操作!");
			/**提交批量操作*/
			psts.executeBatch();
	        conn.commit();
			conn.close();
			
		} catch (SQLException e) {
			log.error("数据库操作出现问题，信息：" + e.getMessage() + "，数据库状态：" + e.getSQLState());
			try{
				/**假如数据库操作出错，则事务回滚且放弃所有的锁*/
				e.printStackTrace();
				if(conn != null){
					if(!conn.isClosed()){
						log.info("事务回滚且放弃所有的锁");
						
						conn.rollback();
						conn.close();
					}
				}
			}catch(SQLException e1){
				log.info("事务回滚失败： " + e.getMessage());
			}
			return false;
		}
		/**清空缓存*/
		log.info("完成持久化操作，清除缓存!");
		pages.clear();
		return true;
		
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
