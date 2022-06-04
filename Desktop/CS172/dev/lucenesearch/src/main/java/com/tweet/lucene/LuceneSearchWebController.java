package com.tweet.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHits;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class LuceneSearchWebController {

	@Autowired
	private LuceneFileIndexBuilder luceneFileIndexBuilder;

	@Autowired
	private ServletContext context;
	
	Logger logger = LoggerFactory.getLogger(LuceneSearchWebController.class);

	@GetMapping({ "/", "/home" })
	public String home(Model m) {
		m.addAttribute("tweet", new Tweet());
		return "home.html";
	}

	@PostMapping("/home")
	public String homeSubmit(@ModelAttribute("tweet") Tweet tweet, Model model) {

		try { 
			//String indexPath = context.getRealPath(LuceneFileIndexBuilder.indexPath);
			logger.info("index path: " + LuceneFileIndexBuilder.indexPath);
			IndexSearcher searcher = luceneFileIndexBuilder.createFileIndex(LuceneFileIndexBuilder.indexPath); 
			logger.info("Device String: " + tweet.getDevice());
			

			TopDocs topDocs = luceneFileIndexBuilder.searchByDevice("device", tweet.getDevice());
			
			logger.info("Tweet text: " + tweet.getText());
			

			//Thread.sleep(5000);
			long totalHitsVal = topDocs.totalHits.value;
			logger.info("TopDocs Total Hits: " + topDocs.totalHits);
			logger.info("TopDocs Total Hits: " + totalHitsVal);
			//String totalHits = Long.toString(topDocs.totalHits.value);
			//logger.debug("Total Hits: " + totalHits);
			model.addAttribute("totalHits", totalHitsVal);
			model.addAttribute("topDocs", topDocs);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			List<Tweet> tweets = new ArrayList<Tweet>();
			for(ScoreDoc scoreDoc : scoreDocs) {
				int docId = scoreDoc.doc;
				Document d = searcher.doc(docId);
				Tweet aTweet = new Tweet();
				aTweet.setId(docId);
				aTweet.setDevice(d.get("device"));
				aTweet.setText(d.get("text"));
				aTweet.setScore(scoreDoc.score);
				tweets.add(aTweet);

			}
			model.addAttribute("tweets", tweets);
			model.addAttribute("scoreDocs", scoreDocs);
		} catch (Exception e) { // TODO
			logger.error(e.getMessage());
			e.printStackTrace();
			return "home";
		}

		return "searchresult";

	}

	@GetMapping("/searchresult")
	public String searchResult(Model model) {

		return "searchresult.html";
	}
}
