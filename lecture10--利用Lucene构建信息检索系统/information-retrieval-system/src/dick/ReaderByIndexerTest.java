package dick;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
 
/**
 * 
 * 通过索引字段来读取文档
 * @author LXY
 *
 */
public class ReaderByIndexerTest {
 
	public static void search(String indexDir,String q)throws Exception{
		
		//得到读取索引文件的路径
		Directory dir=FSDirectory.open(Paths.get(indexDir));
		
		//通过dir得到的路径下的所有的文件
		IndexReader reader=DirectoryReader.open(dir);
		
		//建立索引查询器
		IndexSearcher is=new IndexSearcher(reader);
		
		// 设置为TFIDF 排序
		TFIDFSimilarity sim = new TFIDFSimilarity();
		is.setSimilarity(similarity);
		//实例化分析器
		Analyzer analyzer=new SmartChineseAnalyzer(); 
		
		//建立查询解析器
		/**
		 * 第一个参数是要查询的字段；
		 * 第二个参数是分析器Analyzer
		 * */
		QueryParser parser=new QueryParser("contents", analyzer);
		
		//根据传进来的q查找
		Query query=parser.parse(q);
		
		//计算索引开始时间
		long start=System.currentTimeMillis();
		
		//开始查询
		/**
		 * 第一个参数是通过传过来的参数来查找得到的query；
		 * 第二个参数是要出查询的行数
		 * */
		TopDocs hits=is.search(query, 10);
		
		//计算索引结束时间
		long end=System.currentTimeMillis();
		
		System.out.println("匹配 "+q+" ，总共花费"+(end-start)+"毫秒"+"查询到"+hits.totalHits+"个记录");
		
		//遍历hits.scoreDocs，得到scoreDoc
		/**
		 * ScoreDoc:得分文档,即得到文档
		 * scoreDocs:代表的是topDocs这个文档数组
		 * @throws Exception 
		 * */
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=is.doc(scoreDoc.doc);
			System.out.println(doc.get(LuceneConstants.FILE_PATH));
		}
		
		//关闭reader
		reader.close();
	}
	
	//测试
	public static void main(String[] args) {
		
		String indexDir="C:\\Users\\asus\\Desktop\\java\\information-retrieval-system\\index";
		String q="美利坚合众国";
		
		try {
			search(indexDir,q);
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		     e.printStackTrace();
		}
	}
	
		
}