package com.lucene_engine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class IndexData {

	private static final String FILE_COURSE = "resources/courses.txt";
	private static final String FILE_DEGREE = "resources/degree_abbrs.txt";
	private static final String FILE_LANGUAGE = "resources/languages.txt";
	private static final String FILE_UNIVERSITY = "resources/universities.txt";
	private static final String FILE_UNIVERSITY_LOCATION = "resources/irish_university_locations.txt";

	private static CourseSearchEngine courseSearchEngine;
	private static DegreeSearchEngine degreeSearchEngine;
	private static LanguageSearchEngine languageSearchEngine;
	private static UniversitySearchEngine universitySearchEngine;
	private static UniversityLocationSearchEngine universityLocationSearchEngine;

	public static void main(String[] args) throws Exception {
		initCourseSearchEngine();
		initDegreeSearchEngine();
		initLanguageSearchEngine();
		initUniversitySearchEngine();
		initUniversityLocationSearchEngine();

	}

	public static void initCourseSearchEngine() throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(FILE_COURSE)));
		System.out.println("Reading courses.txt");

		courseSearchEngine = new CourseSearchEngine(
				Statics.COURSE_INDEX_BASE_PATH);
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.isEmpty() == false) {
				courseSearchEngine.addNewDocument(line);
			}
		}
		courseSearchEngine.closeIndexWriter();
		reader.close();
		System.out.println("Finish indexing course data...");
	}

	public static void initDegreeSearchEngine() throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(FILE_DEGREE)));
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

	public static void initLanguageSearchEngine() throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(FILE_LANGUAGE)));
		System.out.println("Reading languages.txt");

		languageSearchEngine = new LanguageSearchEngine(
				Statics.LANGUAGE_INDEX_BASE_PATH);

		String line;
		while ((line = reader.readLine()) != null) {
			if (line.isEmpty() == false) {
				languageSearchEngine.addNewDocument(line);
			}
		}
		languageSearchEngine.closeIndexWriter();
		reader.close();
		System.out.println("Finish indexing language data...");
	}

	public static void initUniversitySearchEngine() throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(FILE_UNIVERSITY)));
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

	public static void initUniversityLocationSearchEngine() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(FILE_UNIVERSITY_LOCATION)));

		System.out.println("Reading irish_university_location");

		universityLocationSearchEngine = new UniversityLocationSearchEngine(
				Statics.LOCATION_INDEX_PATH_PATH);
		String line;
		String universityName = null, location = null;
		while ((line = reader.readLine()) != null) {
			if (line.isEmpty() == false) {
				for (int i = line.length() - 1; i >= 0; --i) {
					if (line.charAt(i) == '-') {
						universityName = line.substring(0, i);
						location = line.substring(i + 1);
						break;
					}
				}
				universityLocationSearchEngine.addNewDocument(universityName,
						location);
			}
		}
		universityLocationSearchEngine.closeIndexWriter();
		reader.close();
		System.out.println("Finish indexing university location data...");
	}
}
