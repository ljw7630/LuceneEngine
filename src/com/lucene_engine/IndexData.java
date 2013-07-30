package com.lucene_engine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class IndexData {

	private static DegreeSearchEngine degreeSearchEngine;
	private static UniversitySearchEngine universitySearchEngine;

	public static void main(String[] args) throws Exception {
		initUniversitySearchEngine();
	}

	public static void initUniversitySearchEngine() throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream("resources/universities.txt")));
		System.out.println("Reading universities.txt");

		universitySearchEngine = new UniversitySearchEngine(
				Statics.UNIVERSITY_INDEX_BASE_PATH);
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.isEmpty() == false) {
				universitySearchEngine.addNewDocument(line);
			}
		}
		universitySearchEngine.closeIndexWriter();
		reader.close();
		System.out.println("Finish indexing university data...");
	}

	public static void initDegreeSearchEngine() throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream("resources/degree_abbrs.txt")));
		System.out.println("Reading degree_abbrs.txt...");

		degreeSearchEngine = new DegreeSearchEngine(
				Statics.DEGREE_INDEX_BASE_PATH);
		String line;
		while ((line = reader.readLine()) != null) {
			String values[] = line.split(" ");
			if (values.length == 0) {
				continue;
			}
			String level = values[0];
			String abbs[] = Arrays.copyOfRange(values, 1, values.length);
			degreeSearchEngine.addNewDocument(level, abbs);
		}
		degreeSearchEngine.closeIndexWriter();
		reader.close();
		System.out.println("Finish indexing degree data...");
	}
}
