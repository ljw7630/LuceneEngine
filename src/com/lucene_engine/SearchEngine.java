package com.lucene_engine;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public abstract class SearchEngine {
	protected IndexWriter writer;
	protected IndexReader reader;
	protected IndexSearcher searcher;
	protected String path;
	
	protected IndexWriter getIndexWriter() throws IOException {
		if (writer == null) {
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43,
					analyzer);
			config.setOpenMode(OpenMode.CREATE);
			Directory directory = FSDirectory.open(new File(path));
			writer = new IndexWriter(directory, config);
		}

		return writer;
	}
	
	public void closeIndexWriter() throws IOException {
		if(writer != null) {
			writer.close();
		}
	}
	
	protected IndexSearcher getIndexSearcher() throws IOException {
		if(searcher == null) {
			reader = DirectoryReader.open(FSDirectory.open(new File(path)));
			searcher = new IndexSearcher(reader);
		}
		return searcher;
	}
	
	public void closeIndexReader() throws IOException {
		reader.close();
	}
}
