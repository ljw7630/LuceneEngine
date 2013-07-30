package com.lucene_engine;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;

public class UniversitySearchEngine extends SearchEngine {

	public UniversitySearchEngine(String path) {
		this.path = path;
	}

	public void addNewDocument(String name) throws Exception {
		Document doc = new Document();
		doc.add(new StringField("keyword", name, Field.Store.YES));
		doc.add(new StringField("contents", name.replace(' ', '_'),
				Field.Store.YES));

		try {
			IndexWriter indexWriter = getIndexWriter();
			indexWriter.addDocument(doc);
			System.out.println(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String query(String queryString) throws Exception {
		IndexSearcher indexSearcher = getIndexSearcher();

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
		QueryParser parser = new QueryParser(Version.LUCENE_43, "contents",
				analyzer);

		queryString = QueryParser.escape(queryString);

		Query query = parser.parse(queryString.replace(" ", "_") + "~0.8");

		System.out.println(query);

		TopDocs results = indexSearcher.search(query, 1);
		ScoreDoc[] hits = results.scoreDocs;

		if (hits.length > 0) {
			Document doc = searcher.doc(hits[0].doc);
			System.out.println(doc.get("keyword") + "\n");

			return doc.get("keyword");
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
				System.out.println(doc.get("keyword") + "\n");
				return doc.get("keyword");
			} else {
				addNewDocument(queryString);
				return queryString;
			}
		}
	}
}
