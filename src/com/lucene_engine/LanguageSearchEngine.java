package com.lucene_engine;

import java.io.IOException;

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

public class LanguageSearchEngine extends SearchEngine {

	public LanguageSearchEngine(String path) {
		this.path = path;
	}

	public void addNewDocument(String language) {
		Document doc = new Document();
		doc.add(new StringField("keyword", language.toLowerCase(), Field.Store.YES));

		doc.add(new TextField("contents", language, Field.Store.YES));

		try {
			IndexWriter indexWriter = getIndexWriter();
			indexWriter.addDocument(doc);
			System.out.println(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String query(String queryString) throws IOException, ParseException {
		IndexSearcher indexSearcher = getIndexSearcher();

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
		QueryParser parser = new QueryParser(Version.LUCENE_43, "contents",
				analyzer);
		
		queryString = QueryParser.escape(queryString);
		
		Query query = parser.parse(queryString + "~");
		
		System.out.println(query);
		
		TopDocs results = indexSearcher.search(query, 1);
		
		ScoreDoc[] hits = results.scoreDocs;
		
		if(hits.length > 0) {
			Document doc = searcher.doc(hits[0].doc);
			System.out.println(doc.get("keyword") + "\n");
			return doc.get("keyword");
		} else {
			addNewDocument(queryString);
			return queryString;
		}
	}
}
