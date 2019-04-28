package dick;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
 
/**
 * 
 * 通过索引字段来读取文档
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
		
		// 设置为TF/IDF 排序
		ClassicSimilarity sim = new ClassicSimilarity();
		// Implemented as sqrt(freq).
		// sim.tf(reader.getSumDocFreq(q));
		
		// Implemented as log((docCount+1)/(docFreq+1)) + 1.
		// sim.idf(reader.getSumDocFreq(q), reader.numDocs());
		is.setSimilarity(sim);
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
	
	public static String[] checkWord(String queryWord){
		//新索引目录
		String spellIndexPath = "C:\\Users\\asus\\Desktop\\java\\information-retrieval-system\\newPath";
		//已有索引目录
		String oriIndexPath = "C:\\Users\\asus\\Desktop\\java\\information-retrieval-system\\index";

		//拼写检查
		try {
			//目录
			Directory directory = FSDirectory.open((new File(spellIndexPath)).toPath());

			SpellChecker spellChecker = new SpellChecker(directory);

			//以下几步用来初始化索引
			IndexReader reader = DirectoryReader.open(FSDirectory.open((new File(oriIndexPath)).toPath()));
			//利用已有索引
			Dictionary dictionary = new LuceneDictionary(reader, LuceneConstants.CONTENTS);
			
			IndexWriterConfig config = new IndexWriterConfig(new SmartChineseAnalyzer());
			spellChecker.indexDictionary(dictionary, config, true);
			
			int numSug = 5;
			String[] suggestions = spellChecker.suggestSimilar(queryWord, numSug);
//			for(String suggestion: suggestions){
//		        System.out.println(suggestion); 
//			}
			reader.close();
			spellChecker.close();
			directory.close();
			return suggestions;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//测试
	public static void main(String[] args) throws IOException {
		String indexDir="C:\\Users\\asus\\Desktop\\java\\information-retrieval-system\\index";
		// 处理输入
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
        String str = null; 
        System.out.println("请输入你要搜索的关键词:"); 
        try {
			str = br.readLine();
			System.out.println(); 
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        // 拼写检查
        String temp = str;
        String[] suggestions = checkWord(str);
        if (suggestions != null && suggestions.length != 0){
            System.out.println("你可能想输入的是:"); 
        	for(int i = 0; i < suggestions.length; i++){
		        System.out.println((i+1) + " : " + suggestions[i]); 
			}

            System.out.println("请选择上面的一个正确的关键词(输入 1 ~ 5)，或继续原词(输入0)进行搜索:"); 
            str = br.readLine();
			System.out.println(); 
            if (str != "0"){
            	str = suggestions[str.charAt(0) - '1'];
            }
            else{
            	str = temp;
            }
        }
		
		try {
			search(indexDir,str);
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		     e.printStackTrace();
		}
	}
	
		
}