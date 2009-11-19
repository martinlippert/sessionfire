package com.sessionfive.app;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RowMaker {

	// Ein möglichst komplizierter Algorithmus um möglichst natürlich nach
	// Trennzeichen / Whitespaces zu trennen. Aber nie mehr als die angegebende
	// Spaltenbreite
	public static List<String> makeRows(String str, int column) {
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer stringTokenizer = new StringTokenizer(str, " \t\n\r\f,.-;", true);
		StringBuffer currentLine = new StringBuffer();
		while (stringTokenizer.hasMoreElements()) {
			String next = stringTokenizer.nextToken();
			if (currentLine.length() + next.length() > column) {
				if (next.length() < 15) {
					// withou leading whitespace
					if (currentLine.toString().subSequence(0, 1).equals(" ")) {
						list.add(currentLine.toString().substring(1));
					} else {
						list.add(currentLine.toString());
					}
					currentLine = new StringBuffer(next);
				} else {
					while (next.length() > 0) {
						int freespaces = column - currentLine.length();
						int endidx = freespaces < column ? freespaces : column;
						endidx = Math.min(endidx, next.length());
						String substring = next.substring(0, endidx);
						currentLine.append(substring);
						next = next.substring(endidx, next.length());
						if (next.length() > 0) {
							currentLine.append("-");
						}
						freespaces = column - currentLine.length();
						if (freespaces < 1) {
							list.add(currentLine.toString());
							currentLine = new StringBuffer();
						}
					}
				}
			} else {

				currentLine.append(next);
			}
		}
		// withou leading whitespace
		if (currentLine.length() > 0 && currentLine.toString().subSequence(0, 1).equals(" ")) {
			list.add(currentLine.toString().substring(1));
		} else {
			list.add(currentLine.toString());
		}

		return list;
	}

	public static void main(String[] args) {
		List<String> makeRows = makeRows("einwirk lichlangerteswerw. sadfasdf asdflsdf. sdf", 20);
		for (String string : makeRows) {
			System.out.println(string);
		}
		makeRows = makeRows(
				"einwirklichlangerteswerwasöldkasödlkasödlkasdölaksdöalsdkasdasdasdsdf", 20);
		for (String string : makeRows) {
			System.out.println(string);
		}
		makeRows = makeRows(
				"ein tweet mit vielen kleinen worten z.b. mit mit und ohne ohne ach ja", 20);
		for (String string : makeRows) {
			System.out.println(string);
		}
		makeRows = makeRows(
				"ein-tweet-mit-vielen-kleinen-worten-z.b.,mit,mit,und,ohne,ohne,ach,ja", 20);
		for (String string : makeRows) {
			System.out.println(string);
		}
		makeRows = makeRows("", 20);
		for (String string : makeRows) {
			System.out.println(string);
		}
	}

}
