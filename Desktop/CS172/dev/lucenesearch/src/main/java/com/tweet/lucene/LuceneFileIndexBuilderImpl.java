package com.tweet.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class LuceneFileIndexBuilderImpl implements LuceneFileIndexBuilder {
	private Analyzer analyzer;

	private IndexWriter indexWriter = null;

	public static final String docsPath = "/Users/rohanbehera/Desktop/CS172/dev/lucenesearch/data";
	public static final String indexPath = "/Users/rohanbehera/Desktop/CS172/dev/lucenesearch/index";
	final Path docDir = Paths.get(docsPath);

	public LuceneFileIndexBuilderImpl() throws IOException {
		super();
		analyzer = new StandardAnalyzer();
	}

	public void cleanupFileIndexFolder(String indexPath) {
		File dir = new File(indexPath);
		final File[] indexFolder = dir.listFiles();
		for (File f : indexFolder)
			f.delete();
	}

	public IndexSearcher createFileIndex(String indexPath) throws IOException, org.json.simple.parser.ParseException {
		// Read from the folder
		Directory indexDir = FSDirectory.open(Paths.get(indexPath));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(OpenMode.CREATE);
		indexWriter = new IndexWriter(indexDir, config);
		// cleanupFileIndexFolder(INDEX_DIRECTORY);
		List<JSONArray> jsonObjectsList = parseInputJsonFile();



		///
		for (Object o : jsonObjectsList) {
			JSONArray jsonArray = (JSONArray)o;
			//for (int i = 0; i < jsonArray.size(); i++) {
			addDocuments(jsonArray);
			//}
			//System.out.println(jsonArray.toJSONString());
		}
		
		indexWriter.commit();
		indexWriter.close();
		return createSearcher();
	}

	// Parse the input JSON file
	public List<JSONArray> parseInputJsonFile() {

		JSONArray arrayObjects = null;
		int fileCount = 0;
		List<JSONArray> jsonArrayList = new ArrayList<JSONArray>();
		if (Files.isDirectory(docDir)) {

			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File f, String name) {
					// We want to find only .c files
					return name.endsWith(".json");
				}
			};
			File f = new File(docsPath);
			File[] files = f.listFiles(filter);
			Reader readerJson = null;
			for (File file : files) {
				InputStream jsonFile;
				try {
					jsonFile = new FileInputStream(file.getAbsolutePath());
					readerJson = new InputStreamReader(jsonFile);

					// Parse the json file using simple-json library
					Object fileObjects = JSONValue.parse(readerJson);
					arrayObjects = (JSONArray) fileObjects;
					jsonArrayList.add(arrayObjects);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				fileCount++;

			}

		}
		return jsonArrayList;

	}

	@SuppressWarnings("unchecked")
	public void addDocuments(JSONArray jsonObjects) {
		/*
		 * for (JSONObject object : (List<JSONObject>) jsonObjects) { Document doc = new
		 * Document(); for (String field : (Set<String>) object.keySet()) { Class type =
		 * object.get(field).getClass(); if (type.equals(String.class)) { doc.add(new
		 * StringField(field, (String) object.get(field), Field.Store.YES)); } else if
		 * (type.equals(Long.class)) { doc.add(new LongPoint(field, (long)
		 * object.get(field))); } else if (type.equals(Double.class)) { doc.add(new
		 * DoublePoint(field, (double) object.get(field))); } else if
		 * (type.equals(Boolean.class)) { doc.add(new StringField(field,
		 * object.get(field).toString(), Field.Store.YES)); } } try {
		 * indexWriter.addDocument(doc); } catch (IOException ex) {
		 * System.err.println("Error adding documents to the index. " +
		 * ex.getMessage()); } }
		 */

		for (Object O : jsonObjects) {

			Document document = new Document();

			JSONObject tweets = (JSONObject) O;

			Long id = 0L;
			if (tweets.get("id") != null) {
				id = (Long) tweets.get("id");
			}

			long user_id = 0L;
			if (tweets.get("user_id") != null) {
				user_id = (Long) tweets.get("user_id");
			}

			String created_at = "";
			if (tweets.get("created_at") != null) {
				created_at = (String) tweets.get("created_at");
			}

			String device = "";
			if (tweets.get("device") != null) {
				device = (String) tweets.get("device");
			}

			String text = "";
			if (tweets.get("text") != null) {
				text = (String) tweets.get("text");
			}

			long likes = 0L;
			if (tweets.get("likes") != null) {
				likes = (Long) tweets.get("likes");
			}

			long retweets = 0L;
			if (tweets.get("retweets") != null) {
				likes = (Long) tweets.get("retweets");
			}

			document.add(new LongPoint("id", (Long) id));
			document.add(new LongPoint("user_id", (Long) user_id));
			document.add(new StringField("created_at", created_at, Field.Store.YES));
			document.add(new StringField("device", device, Field.Store.YES));
			document.add(new StringField("text", text, Field.Store.YES));
			document.add(new LongPoint("likes", (Long) likes));
			document.add(new LongPoint("retweets", (Long) retweets));

			try {
				indexWriter.addDocument(document);
			} catch (IOException ex) {
				System.err.println("Error adding documents to the index. " + ex.getMessage());
			}
		}

	}

	public IndexSearcher createSearcher() throws IOException {
		Directory dir = FSDirectory.open(Paths.get(indexPath));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		return indexSearcher;
	}

	public TopDocs searchById(String key, long id) throws Exception {
		Query query = new QueryParser(key, analyzer).parse(Long.toString(id));

		IndexSearcher searcher = createSearcher();
		TopDocs topDocs = searcher.search(query, 10);
		List<Document> documents = new ArrayList<>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			documents.add(searcher.doc(scoreDoc.doc));
		}

		return topDocs;

	}

	public TopDocs searchByDevice(String key, String deviceStr) throws Exception {
		IndexSearcher searcher = createSearcher();
		/*
		 * BooleanQuery booleanQuery = new BooleanQuery.Builder().build(); Builder
		 * builder = new BooleanQuery.Builder(); String fields[] = { "device" };
		 * 
		 * for (int i = 0; i < fields.length; i++) { TermQuery tq = new TermQuery(new
		 * Term(fields[i], deviceStr)); BoostQuery boostQuery = null; if
		 * (fields[i].equals("device")) { boostQuery = new BoostQuery(tq, 100.0f); }
		 * else { boostQuery = new BoostQuery(tq, 0.0f); } builder.add(new
		 * BooleanClause(boostQuery, Occur.SHOULD));
		 * 
		 * }
		 * 
		 * booleanQuery = builder.build(); System.out.println("_?_____?____Query:" +
		 * booleanQuery.toString()); TopDocs hits = searcher.search(booleanQuery, 10);
		 */
		TermQuery tq = new TermQuery(new Term(key, deviceStr));
		TopDocs hits = searcher.search(tq, 10);
		ScoreDoc[] scoreDocs = hits.scoreDocs;

		for (int n = 0; n < scoreDocs.length; ++n) {
			ScoreDoc sd = scoreDocs[n];
			float score = sd.score;
			int docId = sd.doc;
			Document d = searcher.doc(docId);
			String device = d.get("device");
			String text = d.get("text");
			/* x */
			System.out.printf("%3d %4.2f  %s %s\n", n, score, device, text);
			
		}
		return hits;
	}

	private TopDocs searchByLikesWithBoost(long likes) throws Exception {
		IndexSearcher searcher = createSearcher();
		QueryParser qp = new QueryParser("likes", new StandardAnalyzer());
		Query likesQuery = qp.parse(Long.toString(likes));
		TopDocs hits = searcher.search(likesQuery, 10);
		return hits;
	}

	public TopDocs searchByTermQuery(String key, String value) throws Exception {
		IndexSearcher searcher = createSearcher();

		Term t = new Term(key, value);
		Query query = new TermQuery(t);

		TopDocs hits = searcher.search(query, 10);

		return hits;
	}

	public TopDocs wildCardSearch(String key, String searchFor) throws Exception {
		IndexSearcher searcher = createSearcher();
		Term term = new Term(key, searchFor);

		// create the term query object
		Query query = new WildcardQuery(term);

		System.out.println("\nSearching for '" + query + "' using WildcardQuery");

		// Perform the search
		long startTime = System.currentTimeMillis();

		TopDocs topDocs = searcher.search(query, 10);
		long endTime = System.currentTimeMillis();

		System.out.println("wildCardSearch Total Hits: " + topDocs.totalHits.value);
		System.out.println("************************************************************************");
		System.out.println(
				topDocs.totalHits + " documents found. Time taken for the search :" + (endTime - startTime) + "ms");
		System.out.println("************************************************************************");
		System.out.println("");
		return topDocs;

	}

	public static void main(String[] args) throws IOException {
		LuceneFileIndexBuilderImpl luceneFileIndexBuilderImpl = new LuceneFileIndexBuilderImpl();
		try {
			luceneFileIndexBuilderImpl.createFileIndex(indexPath);

			IndexSearcher searcher = luceneFileIndexBuilderImpl.createSearcher();

			// Search indexed contents using search term
			TopDocs deviceDocs = luceneFileIndexBuilderImpl.searchByDevice("device", "Twitter for iPhone");

			TopDocs queryDocs = luceneFileIndexBuilderImpl.searchByTermQuery("text", "@Marvel_thot No it's a woman's issue and m*n have no place in it.");

			// Total found documents
			System.out.println("SearchByDevice Total Results :: " + deviceDocs.totalHits);
			
			//Total foudn queries
			System.out.println("SearchByQuery total results :: " + queryDocs.totalHits);

			// Let's print out the path of files which have searched term
			for (ScoreDoc sd : deviceDocs.scoreDocs) {
				Document d = searcher.doc(sd.doc);
				System.out.println("Device : " + d.get("device") + ", Score : " + sd.score);
			}

//			TopDocs termDocs = luceneFileIndexBuilderImpl.searchByTermQuery("user_id", "890922581808828417");
//			// Total found documents
//			System.out.println("SearchByTermQuery Total Results :: " + termDocs.totalHits);
//
//			for (ScoreDoc sd : termDocs.scoreDocs) {
//				Document d = searcher.doc(sd.doc);
//				System.out.println("id : " + d.get("id") + ", Score : " + sd.score );
//			}
//
//			TopDocs idDocs = luceneFileIndexBuilderImpl.searchById("id", 890922581808828417L);
//
//			// Total found documents
//			System.out.println("searchById Total Results :: Search With id: " + idDocs.totalHits);
//
//			for (ScoreDoc sd : idDocs.scoreDocs) {
//				Document d = searcher.doc(sd.doc);
//				System.out.println("id : " + d.get("id") + ", Score : " + sd.score);
//			}
//
//			TopDocs wcDocs = luceneFileIndexBuilderImpl.wildCardSearch("device", "Twitter for iPhone*");
//
//			// Total found documents
//			System.out.println("WildCard Search Total Results for device ::Twitter for Android " + wcDocs.totalHits);
//
//			for (ScoreDoc sd : wcDocs.scoreDocs) {
//				Document d = searcher.doc(sd.doc);
//				System.out.println("id : " + d.get("id") + ", Score : " + sd.score);
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
