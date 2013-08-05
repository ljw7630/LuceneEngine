package com.lucene_engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.lucene.queryparser.classic.ParseException;

public class CourseQueryHandler extends Thread {
	private CourseSearchEngine courseSearchEngine;
	private Socket socket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;

	public CourseQueryHandler(Socket socket) throws IOException {
		System.out.println("Create CourseQueryHandler...");
		this.socket = socket;
		this.courseSearchEngine = new CourseSearchEngine(
				Statics.COURSE_INDEX_BASE_PATH);
		this.bufferedReader = new BufferedReader(new InputStreamReader(
				this.socket.getInputStream()));
		this.printWriter = new PrintWriter(this.socket.getOutputStream());
	}

	public void run() {
		while (true) {
			String queryString;
			try {
				System.out
						.println("CourseQueryHandler: Waiting for query string...");
				queryString = bufferedReader.readLine();
				String result = courseSearchEngine.query(queryString);
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
		
		try {
			this.bufferedReader.close();
			this.printWriter.close();
			this.socket.close();
			this.courseSearchEngine.closeIndexReader();
			this.courseSearchEngine.closeIndexWriter();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
