package com.lucene_engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;

public class UniversityQueryHandler extends Thread{
	private UniversitySearchEngine universitySearchEngine;
	private Socket socket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	
	public UniversityQueryHandler(Socket socket) throws IOException {
		System.out.println("Create UniversityQueryHandler...");
		
		this.socket = socket;
		this.universitySearchEngine = new UniversitySearchEngine(Statics.UNIVERSITY_INDEX_BASE_PATH);
		this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.printWriter = new PrintWriter(this.socket.getOutputStream(), true);
	}
	
	public void run() {
		while(true) {
			String queryString;
			try {
				System.out.println("UniversityQueryHandler: Waiting for query string...");
				queryString = bufferedReader.readLine();
				String result = universitySearchEngine.query(queryString);
				System.out.println("Return string: " + result);
				printWriter.println(result);
				printWriter.flush();
				System.out.println();
			} catch(IOException e) {
				break;
			} catch (ParseException e) {
				break;
			} catch (Exception e) {
				break;
			}
		}
		
		try {
			this.bufferedReader.close();
			this.printWriter.close();
			this.socket.close();
			this.universitySearchEngine.closeIndexReader();
			this.universitySearchEngine.closeIndexWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
