package hitwh.yxx.wei.WeiSearch;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IKTest {
	/**
	 * 参考链接：http://blog.csdn.net/lijun7788/article/details/7719166
	 * @throws IOException
	 */
	//@Test
	public void testIK1() throws IOException{
		//String text="基于java语言开发的轻量级的中文分词工具包"; 
		String text="刚还以为是网络问题，重新下载了很多次，都提示maven库里找不到相关jar文件。"
				+ "网上一查，原来是这个驱动包是需要Oracle官方授权才能被我们下载。";
        //创建分词对象  
        Analyzer anal=new IKAnalyzer(true);       
        StringReader reader=new StringReader(text);  
        //分词  
        TokenStream ts=anal.tokenStream("", reader);  
        CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);  
        //遍历分词数据  
        while(ts.incrementToken()){  
            System.out.print(term.toString()+"|");  
        }  
        reader.close();  
        System.out.println();
		
	}
	
	
	@Test
	public void testIK2() throws IOException{
		
		String text="刚还以为是网络问题，重新下载了很多次，都提示maven库里找不到相关jar文件。"
				+ "网上一查，原来是这个驱动包是需要Oracle官方授权才能被我们下载。";; 
        StringReader sr=new StringReader(text);  
        IKSegmenter ik=new IKSegmenter(sr, true);  
        Lexeme lex=null;  
        while((lex=ik.next())!=null){  
            System.out.print(lex.getLexemeText()+"|");  
        }
		
	}
	
	

}
