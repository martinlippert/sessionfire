package com.sessionfive.app;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RowMaker {

	public static List<String> makeRows(String str, int column) {
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer stringTokenizer = new StringTokenizer(str);
		StringBuffer currentLine = new StringBuffer();
		while (stringTokenizer.hasMoreElements()) {
			currentLine.append(stringTokenizer.nextToken() + " ");
			if(currentLine.length() > column) {
				list.add(currentLine.toString());
				currentLine = new StringBuffer();
			}
		}
		list.add(currentLine.toString());
		return list;
	}
	

}
