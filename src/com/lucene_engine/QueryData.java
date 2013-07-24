package com.lucene_engine;




public class QueryData {
	private static DegreeSearchEngine degreeSearchEngine;
	

	public static void main(String[] args) throws Exception {
		initDegreeSearchEngine();
		degreeSearchEngine.closeIndexReader();
		degreeSearchEngine.closeIndexWriter();
//		BufferedReader in = null;
//		in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
//		while(true) {
//			System.out.println("Query string:");
//			
//			String input = in.readLine();
//			
//			input = input.trim().replace(',', ' ');
//			
//			SimpleEntry<String, String> entry = degreeSearchEngine.query(input);
//			
//			if (entry != null) {
//				System.out.println(entry.getKey() + " " + entry.getValue());
//			} else {
//				System.out.println("not found");
//			}
//		}
	}
	

	
	public static void initDegreeSearchEngine() {
		degreeSearchEngine = new DegreeSearchEngine(Statics.DEGREE_INDEX_BASE_PATH);
	}
}
