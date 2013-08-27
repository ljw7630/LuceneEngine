package com.lucene_engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Handler {
	private static ServerSocket serverSocket;

	public Handler(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	public void start() throws Exception {
		while (true) {
			try {
				System.out.println("Waiting for next client....");
				Socket client = serverSocket.accept();
				System.out.println("A new client enter...");

				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(client.getInputStream()));
				String commandString = bufferedReader.readLine();

				System.out.println("Command: " + commandString);
				
				String engine = bufferedReader.readLine();
				
				if (commandString.equals("index")) {

					if (engine.equals("DegreeSearchEngine")) {
						IndexData.initDegreeSearchEngine();
					} else if (engine.equals("CourseSearchEngine")) {
						IndexData.initCourseSearchEngine();
					} else if (engine.equals("PositionSearchEngine")) {

					} else if (engine.equals("LanguageSearchEngine")) {

					} else if (engine.equals("UniversitySearchEngine")) {
						IndexData.initUniversitySearchEngine();
					} else if (engine.equals("UniversityLocationSearchEngine")) {
						IndexData.initUniversityLocationSearchEngine();
					}

				} else if (commandString.equals("query")) {
					if (engine.equals("DegreeSearchEngine")) {
						DegreeQueryHandler degreeQueryHandler = new DegreeQueryHandler(
								client);
						degreeQueryHandler.start();
					} else if (engine.equals("CourseSearchEngine")) {
						CourseQueryHandler courseQueryHandler = new CourseQueryHandler(
								client);
						courseQueryHandler.start();
					} else if (engine.equals("PositionSearchEngine")) {

					} else if (engine.equals("LanguageSearchEngine")) {
						LanguageQueryHandler languageQueryHandler = new LanguageQueryHandler(
								client);
						languageQueryHandler.start();
					} else if (engine.equals("UniversitySearchEngine")) {
						UniversityQueryHandler universityQueryHandler = new UniversityQueryHandler(
								client);
						universityQueryHandler.start();
					} else if (engine.equals("UniversityLocationSearchEngine")) {
						UniversityLocationQueryHandler universityLocationQueryHandler = new UniversityLocationQueryHandler(
								client);
						universityLocationQueryHandler.start();
					}

				} else {
					System.out.println("Unknown command");
					client.close();
				}

			} catch (IOException e) {

			}
		}
	}

	public static void main(String args[]) throws Exception {
		int port;
		if(args.length != 1) {
			System.out.println("using " + Statics.SERVER_PORT + " as port");
			port = Statics.SERVER_PORT;
		} else {
			port = Integer.parseInt(args[0]);
			System.out.println("using " + port + " as port");
		}
		Handler handler = new Handler(port);
		handler.start();
	}
}
