package dick;

import java.io.File;
import java.io.IOException;

public class LuceneTester {
	
   String indexDir = "C:/Users/asus/Desktop/java/information-retrieval-system/index";
   String dataDir = "C:/Users/asus/Desktop/java/information-retrieval-system/data";
   Indexer indexer;
   
   public static void main(String[] args) {
      LuceneTester tester;
//      File[] fs = new File("C:/Users/asus/Desktop/java/information-retrieval-system/data").listFiles();
//      for (File f : fs){  
//          System.out.println(f);  
//      }  
      try {
         tester = new LuceneTester();
         tester.createIndex();
      } catch (IOException e) {
         e.printStackTrace();
      } 
   }

   private void createIndex() throws IOException{
      indexer = new Indexer(indexDir);
      int numIndexed;
      long startTime = System.currentTimeMillis();	
      numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
      long endTime = System.currentTimeMillis();
      indexer.close();
      System.out.println(numIndexed+" File indexed, time taken: "
         +(endTime-startTime)+" ms");		
   }
}


