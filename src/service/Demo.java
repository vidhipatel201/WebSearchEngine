package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Demo {

	private static String htmlPath = System.getProperty("user.dir") + "\\HTML_Files\\";
	private static String textPath = System.getProperty("user.dir") + "\\Text_Files\\";
	public static ArrayList<TST<Integer>> myTST = new ArrayList<>();
	public static Set<String> words = new HashSet<>();
	private static String[] myHTMLFiles;
	

	public static void main(String[] args) throws Exception {

		File myFolder = new File(htmlPath);
		myHTMLFiles = myFolder.list();

		// Converting html to text
//		long s = System.currentTimeMillis();
		htmlToText(myHTMLFiles);
//		long e = System.currentTimeMillis();
//		System.out.println(e-s);

		// reading text folder
		myFolder = new File(textPath);
		String[] files = myFolder.list();

		// reading each text file

		for (String f : files) {

			TST<Integer> tst = new TST<>();
			String[] tokens = textTokenizing(f);

			// storing the tokens into TST

			for (int i = 0; i < tokens.length; i++) {

				// Calculating frequency of word and storing it as value in
				// TST.(taking more Time)

				String temp = tokens[i].replaceAll("[^a-zA-Z]", "");
				words.add(temp);
				if (temp.length() > 0) {
					if (tst.contains(temp)) {
						tst.put(temp, tst.get(temp) + 1);
					} else {
						tst.put(temp, 1);
					}

				}

			}

			myTST.add(tst);

		}

		
		System.out.println("Pre-processing Done.!");
		/*List<String> res = finalCall("python");
		for(String x:res){
			System.out.println(x);
		}*/
		

		List<String> sm = finalCall("programming language");
		for (String s:sm) {
			System.out.println(s);
		}
	}
	
	public static List<String> finalCall(String input){
		
		String searchWord = input.toLowerCase();
		String[] keywords = generateKeyword(searchWord);
		
		String sw = wordSuggestion(keywords);
		if(sw.trim().equals(String.join(" ", keywords))){
			sw="";
		}
		
		
		List<String> result = new ArrayList<>();
		result.add(sw);
		int[][] f = search(keywords);
		Arrays.sort(f, new Comparator<int[]>() {

			@Override
			public int compare(final int[] entry1, final int[] entry2) {

				if (entry1[0] < entry2[0])
					return 1;
				else
					return -1;
			}
		});

		/*for (int i = 0; i < f.length; i++) {
			System.out.println(f[i][0] + " " + f[i][1]);
		}*/

		for (int i = 0; i < 30; i++) {
			if (i > f.length - 1)
				break;
			int index = f[i][1];
			if(f[i][0] == 0) {
				break;
			}
//			System.out.println(myHTMLFiles[index]);
			result.add(myHTMLFiles[index]);
		}
		
		return result;
	}
	

	// Converting HTML files to Text files
	public static void htmlToText(String[] myHTMLFiles) {

		try {
			for (int i = 0; i < myHTMLFiles.length; i++) {

				File myFile = new File(htmlPath + myHTMLFiles[i]);
				Document doc = Jsoup.parse(myFile, "UTF-8");
				String text = doc.text();

				PrintWriter out = new PrintWriter(textPath + myHTMLFiles[i].replaceAll(".html", ".txt"));
				out.println(text);
				out.close();

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	// Returning Tokens of a file
	public static String[] textTokenizing(String f) {

		String[] tokens = null;
		try {

			String fName = textPath + f;
			File myTextFile = new File(fName);

			// storing file content into string
			BufferedReader reader = new BufferedReader(new FileReader(myTextFile));
			String line = null;
			String myStr; 
			StringBuilder stringBuilder = new StringBuilder();
			String ls = System.getProperty(" ");

			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			reader.close();
			myStr = stringBuilder.toString().toLowerCase();

			// Tokenizing the file content
			tokens = myStr.split(" ");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return tokens;

	}

	public static int[][] search(String[] keywords) {

		int[][] freqList = new int[myTST.size()][2];
		TST<Integer> tst;
		for (int i = 0; i < myTST.size(); i++) {
			tst = myTST.get(i);
			freqList[i][0] = 0;
			freqList[i][1] = i;
			for (String w : keywords) {
				if (tst.contains(w)) {
					freqList[i][0] += tst.get(w);
				}
			}
		}
		return freqList;

	}

	public static String[] generateKeyword(String keyword) {

		ArrayList<String> stopWords = new ArrayList<>();
		String[] filteredKeywords = {};
		String line;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\stopWords.txt"));

			while ((line = reader.readLine()) != null) {
				stopWords.add(line);
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] keywords = keyword.split(" ");
		StringBuilder builder = new StringBuilder();
		for (String word : keywords) {
			word = word.trim();
			if (!stopWords.contains(word)) {
				builder.append(word + "\n");
			}
		}

		filteredKeywords = builder.toString().split("\n").clone();

		return filteredKeywords;

	}
	
	public static String wordSuggestion(String[] input){
		
		String suggestedWord = "";
		int eD = 1000;
		int dist;
		String w = null;
		for(String s:input){
			for(String t:words){
				dist = Sequences.editDistance(s, t);
				if(dist < eD){
					eD = dist;
					w = t;
				}
			}
			suggestedWord += w + " ";
			eD = 1000;
			
		}
		
		return suggestedWord;
	}
}
