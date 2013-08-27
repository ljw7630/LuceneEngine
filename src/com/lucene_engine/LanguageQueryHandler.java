package com.lucene_engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.lucene.queryparser.classic.ParseException;

public class LanguageQueryHandler extends Thread{
	private LanguageSearchEngine languageQueryEngine;
	private Socket socket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;

	public LanguageQueryHandler(Socket socket) throws IOException {
		System.out.println("Create LanguageQueryHandler...");
		this.socket = socket;
		this.languageQueryEngine = new LanguageSearchEngine(
				Statics.LANGUAGE_INDEX_BASE_PATH);
		this.bufferedReader = new BufferedReader(new InputStreamReader(
				this.socket.getInputStream()));
		this.printWriter = new PrintWriter(this.socket.getOutputStream(), true);
	}

	public void run() {
		while (true) {
			String queryString;

			try {
				System.out
						.println("LanguageQueryHandler: Waiting for query string...");
				queryString = bufferedReader.readLine();
				String result = languageQueryEngine.query(queryString);

				System.out.println("Return string: " + result);
				printWriter.println(result);
				printWriter.flush();
				System.out.println();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (ParseException e) {
				e.printStackTrace();
				break;
			}
		}
		
		try {
			this.bufferedReader.close();
			this.printWriter.close();
			this.socket.close();
			this.languageQueryEngine.closeIndexReader();
			this.languageQueryEngine.closeIndexWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
