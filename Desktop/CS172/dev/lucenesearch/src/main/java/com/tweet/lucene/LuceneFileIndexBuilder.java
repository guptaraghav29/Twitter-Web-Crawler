package com.tweet.lucene;

import java.io.IOException;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.json.simple.parser.ParseException;

public interface LuceneFileIndexBuilder {
	public static final String docsPath = "/Users/rohanbehera/Desktop/CS172/dev/lucenesearch/data";
	public static final String indexPath = "/Users/rohanbehera/Desktop/CS172/dev/lucenesearch/index";

	IndexSearcher createFileIndex(String docsPath) throws IOException, ParseException;
	
	TopDocs searchByDevice(String key, String deviceStr) throws Exception;
	
	TopDocs wildCardSearch(String key, String deviceStr) throws Exception;
	
	
}
