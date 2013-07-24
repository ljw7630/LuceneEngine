package com.lucene_engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.AbstractMap.SimpleEntry;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

public class DegreeQueryHandler extends Thread{
	
	private DegreeSearchEngine degreeSearchEngine;
	private Socket socket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	
	public DegreeQueryHandler(Socket socket) throws IOException{
		System.out.println("Create DegreeQueryHandler...");
		this.socket = socket;
		this.degreeSearchEngine = new DegreeSearchEngine(Statics.DEGREE_INDEX_BASE_PATH);
		this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.printWriter = new PrintWriter(this.socket.getOutputStream(), true);
	}
	
	public void run() {
		while(true) {		
			String queryString;
			try {
				System.out.println("DegreeQueryHandler: Waiting for query string...");
				queryString = bufferedReader.readLine();
				SimpleEntry<String, String> entry = degreeSearchEngine.query(queryString);
				
				String returnString = entry!=null?entry.getKey() + "," + entry.getValue():""+","+"";
				System.out.println("Return string: " + returnString);
				printWriter.println(returnString);
				printWriter.flush();
				System.out.println();
			} catch (IOException e) {
				// e.printStackTrace();
				break;
			} catch (ParseException e) {
				// e.printStackTrace();
				break;
			} catch (InvalidTokenOffsetsException e) {
				// e.printStackTrace();
				break;
			}
		}
		
		try {
			this.bufferedReader.close();
			this.printWriter.close();
			this.socket.close();
			this.degreeSearchEngine.closeIndexReader();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
