package com.tweet.lucene;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;

public interface LuceneFileSearcher {

	
	TopDocs searchInContent(String textToFind, IndexSearcher indexSearcher) throws ParseException, IOException;

}
