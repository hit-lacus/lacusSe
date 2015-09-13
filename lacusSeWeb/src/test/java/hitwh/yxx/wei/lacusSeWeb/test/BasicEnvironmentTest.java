package hitwh.yxx.wei.lacusSeWeb.test;

import static org.junit.Assert.assertEquals;

import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BasicEnvironmentTest {

	public static void main(String[] args) {

	}
	
	@Test
	public void testSpringEnvironment(){
		/*
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	    User person = (User) context.getBean("yxx");
	    assertEquals("hitwh_yxx@163.com",person.getEmail());*/
	    
	}
	
	@Test
	public void testHibernateEnvironment(){
		/*
		//加载指定目录下的配置文件，得到configuration对象
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        //根据configuration对象得到session工厂对象
        SessionFactory factory = cfg.buildSessionFactory();
        //使用工厂类打开一个session
        Session session = factory.openSession();
        //开启事务
        Transaction tx = session.beginTransaction();
        //创建待插入数据库的对象
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        User p = (User) context.getBean("yxx");
        p.setEmail(UUID.randomUUID().toString());
        p.setName(UUID.randomUUID().toString());
        //保存对象
        session.save(p);
        //提交事务
        tx.commit();
        //关闭资源
        session.close();
        factory.close();*/
	}

}
