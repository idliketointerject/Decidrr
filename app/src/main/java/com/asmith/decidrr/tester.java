package com.asmith.decidrr;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alex on 3/27/2015.
 * traverses the built tree and produces accuracy
 */
class tester {

    private final compacter x = new compacter();


    //calls all the cleaner functions and returns a clean arraylist
    public ArrayList<ArrayList<String>> loadTest(BufferedReader in, node globalRoot, int answerKnown) throws IOException {
        String line;

        int numlines = 0;
        double accuracy;
        int totalCorrect = 0;
        ArrayList<ArrayList<String>> records = new ArrayList<ArrayList<String>>();

        long startTimeClassify = System.currentTimeMillis();

        while((line = in.readLine()) != null){
            if(line.length() > 0){
                String[] words = line.split("\t");
                String cleanLine;
                if(answerKnown >= 1){
                    //words = line.split("\t");
                    cleanLine = x.takeOutTheTrash(words[1]);
                }
                else{
                    //first position is entire line
                    //words = line.split("\t");
                    cleanLine = x.takeOutTheTrash(words[0]);
                }
                String[] arrayize = cleanLine.split(" ");
                ArrayList<String> removedStop = x.removeStopWords(arrayize);

                int prediction = traverseTree(globalRoot,removedStop);
                if(prediction < 0) prediction = 0;
                if(prediction > 0) prediction = 1;
                numlines++;
                if(answerKnown >= 1) {
                    String polarity = words[0];
                    int actual = Integer.parseInt(polarity);
                    if(actual == prediction) totalCorrect++;
                    System.out.println("Predicted : " + prediction + " Actual : " + actual);

                }
                else{
                    if(prediction >= 1) {
                        System.out.println(removedStop);
                    }
                    //System.out.println("Predicted : " + prediction);
                }

                //add to array/compare against actual, compute accuracy
                records.add(removedStop);
            }
        }

        long endTimeClassify = System.currentTimeMillis();
        //insert time to build tree
        //insert time to label tests
        if(answerKnown == 1){
            accuracy = (double)totalCorrect/(double)numlines;
            System.out.println(accuracy + " Accuracy");
        }
        System.out.println((double)(endTimeClassify - startTimeClassify)/(double)1000 + " seconds");
        return records;
    }
    private HashMap<ArrayList<String>,Integer> summedPolarities = new HashMap<ArrayList<String>,Integer>();

    private int traverseTree(node root, ArrayList<String> msg){
        //traverse tree, if you cant find a matching word-node, pick the Left
        int failureCondition = 0;
        int countFailures = 0;
        int lastPol = 999;

        for(int i=0;i<msg.size();i++){
            if(msg.get(i).contains(root.getContents())){
                int polarity = root.getPolarity();
                boolean wut = summedPolarities.containsKey(msg);
                if(wut){
                    int temp = summedPolarities.get(msg);
                    temp += polarity;
                    summedPolarities.remove(msg);
                    summedPolarities.put(msg,temp);
                }
                if(!wut){
                    summedPolarities.put(msg, polarity);
                }
                failureCondition += polarity;
                if(polarity >= 1){
                    if(root.getLeft().size() > 0){
                        lastPol = traverseTree(root.getLeft().get(0), msg);
                    }
                    if(root.getLeft().size() == 0){
                        Integer existsInMap = summedPolarities.get(msg);
                        if(existsInMap != null){
                            return existsInMap;
                        }
                        else{
                            return polarity;
                        }
                    }
                }
                else{
                    //negatives on the right
                    if(root.getRight().size() > 0){
                        lastPol = traverseTree(root.getRight().get(0), msg);
                    }
                    if(root.getRight().size() == 0){
                        Integer existsInMap = summedPolarities.get(msg);
                        if(existsInMap != null){
                            return existsInMap;
                        }
                        else{
                            return polarity;
                        }
                    }
                }
            }
            else{
                countFailures++;
                lastPol = root.getPolarity();
            }
        }
        if(countFailures >= msg.size()){
            if(root.getRight().size() > 0){
                lastPol = traverseTree(root.getRight().get(0), msg);
            }
            if(root.getLeft().size() > 0){
                lastPol = traverseTree(root.getLeft().get(0), msg);
            }
            //else{
            //	lastPol = current.getPolarity();
            //}
        }

        Integer existsInMap = summedPolarities.get(msg);
        if(existsInMap != null){
            return existsInMap;
        }
        else{
            return lastPol;
        }

    }
}
