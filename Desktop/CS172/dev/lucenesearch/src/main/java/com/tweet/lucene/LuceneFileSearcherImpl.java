package com.tweet.lucene;

// import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneFileSearcherImpl implements LuceneFileSearcher {
	private static final String INDEX_DIR = "C:/dev/lucenesearch/index" ;

	public IndexSearcher createSearcher() throws IOException {
		Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		return indexSearcher;
	}
	
	public TopDocs searchInContent(String textToFind, IndexSearcher indexSearcher) throws ParseException, IOException {
        QueryParser qp = new QueryParser(textToFind, new StandardAnalyzer());
        Query query = qp.parse(textToFind);
        
        TopDocs hits = indexSearcher.search(query, 10);
		return hits;

        
	}
	
	private TopDocs searchByDevice(String device, IndexSearcher searcher) throws Exception
	{
		QueryParser qp = new QueryParser(device, new StandardAnalyzer());
		Query deviceQuery = qp.parse(device);
		TopDocs hits = searcher.search(deviceQuery, 10);
		return hits;
	}
    
    public TopDocs searchByTermQuery(String term, String key) throws IOException, ParseException {
        IndexSearcher searcher = createSearcher();

        Term t = new Term(term, key);
        Query query = new TermQuery(t);

        TopDocs hits = searcher.search(query, 10);
        
        return hits;
    }
    public static void main(String[] args) throws Exception 
    {
    	LuceneFileSearcherImpl luceneFileSearcher = new LuceneFileSearcherImpl();
        //Create lucene searcher. It search over a single IndexReader.
        IndexSearcher searcher = luceneFileSearcher.createSearcher();
         
        //Search indexed contents using search term
        TopDocs foundDocs = luceneFileSearcher.searchByDevice("Twitter for iPhone", searcher);
         
        //Total found documents
        System.out.println("Total Results :: " + foundDocs.totalHits);
        
         
        //Let's print out the path of files which have searched term
        for (ScoreDoc sd : foundDocs.scoreDocs) 
        {
            Document d = searcher.doc(sd.doc);
            System.out.println("Device : "+ d.get("device") + ", Score : " + sd.score);
        }

        TopDocs termDocs = luceneFileSearcher.searchByTermQuery("device", "Twitter for iPhone");
        //Total found documents
        System.out.println("Total Results :: " + termDocs.totalHits);
        
        for (ScoreDoc sd : termDocs.scoreDocs) 
        {
            Document d = searcher.doc(sd.doc);
            System.out.println("device : "+ d.get("device") + ", Score : " + sd.score);
        }
       
        TopDocs contentDocs = luceneFileSearcher.searchInContent("890922581808828417", searcher);
        
        //Total found documents
        System.out.println("Total Results :: " + contentDocs.totalHits);
       
        for (ScoreDoc sd : contentDocs.scoreDocs) 
        {
            Document d = searcher.doc(sd.doc);
            System.out.println("id : "+ d.get("id") + ", Score : " + sd.score);
        }
        
        
    }	
}
