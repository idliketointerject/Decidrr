package com.asmith.decidrr;
import java.io.*;
import java.util.*;

public class DecisionTreeClassifier {
	public static void main( String[] args ){
		//args[0] is training
		//args[1] is test
		//args[2] tells whether the answer is known or not(for accuracy testing)
		try{
			String isKnown = args[2];
			int answerKnown = Integer.parseInt(isKnown);
			BufferedReader trainfile = new BufferedReader(new FileReader(args[0]));
			BufferedReader testfile = new BufferedReader(new FileReader(args[1]));

			trainer tr = new trainer();
			tester te = new tester();
			try{
				tr.loadTrain(trainfile);
				node root = tr.getGlobalRoot();
				ArrayList<ArrayList<String>> msgs = te.loadTest(testfile, root,answerKnown);

			} catch (IOException e) {e.printStackTrace();}
		} catch (FileNotFoundException e) {e.printStackTrace();}

	}

}
