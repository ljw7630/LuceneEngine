package com.lucene_engine;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;

public class DegreeSearchEngine extends SearchEngine {

	public DegreeSearchEngine(String path) {
		this.path = path;
	}

	public void addNewDocument(String level, String[] arr) throws Exception {
		Document doc = new Document();
		if (arr.length == 0) {
			throw new Exception("insert nothing to the document");
		}
		doc.add(new StringField("keyword", arr[0], Field.Store.YES));
		doc.add(new StringField("level", level, Field.Store.YES));
		for (String item : arr) {
			// item = item.replace('_', ' ');
			doc.add(new TextField("contents", item, Field.Store.YES));
		}

		try {
			IndexWriter indexWriter = getIndexWriter();
			indexWriter.addDocument(doc);
			System.out.println(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SimpleEntry<String, String> query(String queryString)
			throws IOException, ParseException {
		System.out.println("DegreeSearchEngine: " + queryString);

		IndexSearcher indexSearcher = getIndexSearcher();

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
		QueryParser parser = new QueryParser(Version.LUCENE_43, "contents",
				analyzer);

		queryString = QueryParser.escape(queryString);

		/* Fuzzy search over a long string first */

		Query query = parser.parse(queryString.replace(" ", "_") + "~0.7");

		System.out.println(query);

		TopDocs results = indexSearcher.search(query, 1);
		ScoreDoc[] hits = results.scoreDocs;

		// for(ScoreDoc hit: hits) {
		//
		// Document doc = searcher.doc(hit.doc);
		// System.out.println(doc.get("contents"));
		// System.out.println(hit.score + "\n" + doc);
		// }

		if (hits.length > 0) {
			Document doc = searcher.doc(hits[0].doc);
			System.out.println(doc.get("keyword") + " " + doc.get("level")
					+ "\n");
			return new SimpleEntry<String, String>(doc.get("keyword"),
					doc.get("level"));
		} else {
			/*
			 * Cannot find any document? Split the string into space-seperated
			 * query string and try again
			 */
			System.out
					.println("cannot find any match documents! try another approach...");

			query = parser.parse(queryString);
			System.out.println(query);

			results = indexSearcher.search(query, 1);
			hits = results.scoreDocs;

			if (hits.length > 0) {
				Document doc = searcher.doc(hits[0].doc);
				System.out.println(doc.get("keyword") + " " + doc.get("level")
						+ "\n");
				return new SimpleEntry<String, String>(doc.get("keyword"),
						doc.get("level"));
			} else {
				System.out.println("Still no luck. return null...");
				return null;
			}
		}
	}
}
