package hitwh.yxx.wei.WeiSearch;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class LuceneStart {
	
	@Test
	public void helloLucene() throws IOException{
		Analyzer a = new StandardAnalyzer(Version.LUCENE_36);
		Directory dir = new RAMDirectory(); 
		IndexWriterConfig iwf =new IndexWriterConfig(Version.LUCENE_36 ,a);
		
		IndexWriter writer = new IndexWriter(dir,iwf); 
		Document doc = new Document(); 
		doc.add(new Field("title", "lucene introduction", Field.Store.YES, Field.Index.ANALYZED)); 
		doc.add(new Field("content", "lucene works well", Field.Store.YES, Field.Index.ANALYZED)); 
		writer.addDocument(doc); 
		writer.optimize(); 
		writer.close();
		
		Term t = new Term("content", " lucene"); 
		Query query = new TermQuery(t);
		
		
		
		
	}

}
