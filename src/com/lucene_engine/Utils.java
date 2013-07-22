package com.lucene_engine;

public class Utils {
	public static String splitAndJoin(String queryString, String splitString, String joinString) {
		String[] parts = queryString.split(splitString);
		StringBuilder stringBuilder = new StringBuilder();
		for(int i=0;i<parts.length-1;++i){
			stringBuilder.append(parts[i] + joinString + " ");
		}
		return stringBuilder.toString() + parts[parts.length-1] + joinString;
	}
}
