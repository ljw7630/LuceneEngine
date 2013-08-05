package com.lucene_engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.lucene.queryparser.classic.ParseException;

public class UniversityLocationQueryHandler extends Thread {
	private UniversityLocationSearchEngine universityLocationSearchEngine;
	private Socket socket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;

	public UniversityLocationQueryHandler(Socket socket) throws IOException {
		System.out.println("Create UniversityLocationQueryHandler...");
		this.socket = socket;
		this.universityLocationSearchEngine = new UniversityLocationSearchEngine(
				Statics.LOCATION_INDEX_PATH_PATH);
		this.bufferedReader = new BufferedReader(new InputStreamReader(
				this.socket.getInputStream()));
		this.printWriter = new PrintWriter(this.socket.getOutputStream());
	}

	public void run() {
		while (true) {
			String queryString;
			try {
				System.out
						.println("UniversityLocationQueryHandler: Waiting for query string...");
				queryString = bufferedReader.readLine();
				String result = universityLocationSearchEngine
						.query(queryString);

				if (result == null) {
					result = "None";
				}

				System.out.println("Return string: " + result);
				printWriter.println(result);
				printWriter.flush();
				System.out.println();
			} catch (IOException e) {
				break;
			} catch (ParseException e) {
				break;
			}
		}
		
		try{
			this.bufferedReader.close();
			this.printWriter.close();
			this.socket.close();
			this.universityLocationSearchEngine.closeIndexReader();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
