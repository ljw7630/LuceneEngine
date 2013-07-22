package com.lucene_engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Handler {
	private static ServerSocket serverSocket;

	public Handler() throws IOException {
		serverSocket = new ServerSocket(Statics.SERVER_PORT);
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
				if (commandString.equals("index")) {
					String engine = bufferedReader.readLine();
					if (engine.equals("DegreeSearchEngine")) {
						IndexData.initDegreeSearchEngine();
					} else if (engine.equals("CourseSearchEngine")) {

					} else if (engine.equals("PositionSearchEngine")) {

					}

				} else if (commandString.equals("query")) {
					DegreeQueryHandler degreeQueryHandler = new DegreeQueryHandler(
							client);
					degreeQueryHandler.start();
				} else {
					System.out.println("Unknown command");
					client.close();
				}

			} catch (IOException e) {

			}
		}
	}

	public static void main(String args[]) throws Exception {
		Handler handler = new Handler();
		handler.start();
	}
}
