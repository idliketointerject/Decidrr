package com.asmith.decidrr;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by alex on 3/27/2015.
 * builds the tree from given training data and returns the root node
 */
class trainer {
    private node globalRoot;
    private final compacter x = new compacter();

    public void loadTrain(BufferedReader in) throws IOException {
        String l;
        HashMap<String,Integer> frequencies = new HashMap<String,Integer>();
        while((l = in.readLine())!=null){
            if(l.length()>0){
                String[] words = l.split("\t");
                String polarity = words[0];
                int pol = Integer.parseInt(polarity);

                String cleanLine = x.takeOutTheTrash(words[1]);
                String[] text = cleanLine.split(" ");
                ArrayList<String> removedStopSet = x.removeStopWords(text);

                //get top 10 and bottom 10 frequencies
                for (String aRemovedStopSet : removedStopSet) {
                    //add each remaining word to hash table, increment the value of second
                    //if the same string is encountered
                    //a positive count represents a positive polarity, as well as a relatively high frequency
                    //while a negative count represents a negative polarity and high freq
                    if (frequencies.containsKey(aRemovedStopSet)) {
                        int count = frequencies.get(aRemovedStopSet);
                        frequencies.remove(aRemovedStopSet);
                        if (pol == 1) {
                            frequencies.put(aRemovedStopSet, count + 1);
                        } else {
                            frequencies.put(aRemovedStopSet, count - 1);
                        }
                    } else {
                        //add it to the map
                        if (pol == 1) {
                            frequencies.put(aRemovedStopSet, 1);
                        } else {
                            //pol=0
                            frequencies.put(aRemovedStopSet, -1);
                        }
                    }
                }
            }
        }
        node stubNode = new node(0, "Root",0.0);
        long startTimeTree = System.currentTimeMillis();

        buildTree(frequencies, stubNode);
        long endTimeTree = System.currentTimeMillis();
        System.out.println("Tree built in : " + (double)(endTimeTree - startTimeTree)/(double)1000 + " seconds");

    }

    private static HashMap<String, Integer> sortByVal(Map<String, Integer> map){
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String,Integer>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String,Integer> m1, Map.Entry<String,Integer> m2) {
                return (m2.getValue()).compareTo(m1.getValue());
            }
        });

        HashMap<String,Integer> result = new LinkedHashMap<String, Integer>();
        for(Map.Entry<String,Integer> entry : list){
            result.put(entry.getKey(),entry.getValue());
        }

        return result;
    }

    private node buildTree(HashMap<String, Integer> frequencies, node root){
        //sort by frequency
        //calculate the entropy/gain
        //recursively create nodes

        //sort frequencies
        HashMap<String, Integer> sortedFreq = sortByVal(frequencies);

        //node array for nodes to build the tree with
        ArrayList<node> nodeBin = new ArrayList<node>();

        for(Map.Entry<String,Integer> entry : sortedFreq.entrySet()){
            //calculate entropies

            double entropy = calculateEntropy(entry.getValue());

            //should probably make the conditional varry based off
            //of the average or something
            if(entropy < -37.0){
                //lets make these into nodes
                //sort them and assign links outside of this loop
                node current = new node(entry.getValue(),entry.getKey(),entropy);
                nodeBin.add(current);
            }
        }

        //go through node list, set highest entrop as root, then link it on the left
        //to the second highest, and on the right to the lowest
        //until all nodes are used, but tree is complete, e.g. 1 + 2^height

        node rootNode = nodeSorter(nodeBin);
        return rootNode;
    }

    private node nodeSorter(ArrayList<node> nodes){

        //takes array of nodes to be used in tree, connects them based on their entropy, returns the root
        for(int i=0;i<nodes.size();i++){
            for(int j=0; j< nodes.size()-1;j++){
                node current = nodes.get(j);
                node next = nodes.get(j+1);
                double entropyCurr = current.getEntropy();
                double entropyNext = next.getEntropy();
                if(entropyNext < entropyCurr){
                    //this node is higher on the tree
                    node temp = next;
                    nodes.set(j+1,current);
                    nodes.set(j,temp);
                }

            }
        }

        ArrayList<node> positiveSet = new ArrayList<node>();
        ArrayList<node> negativeSet = new ArrayList<node>();

        for (node node : nodes) {
            int currPol = node.getPolarity();

            if (currPol > 0) {
                positiveSet.add(node);
            } else {
                negativeSet.add(node);
            }
        }

        ArrayList<node> mergeSet = new ArrayList<node>();

        for(int i=0;i<nodes.size();i++){
            if(i%2 == 0){
                if(positiveSet.size() > 0){
                    mergeSet.add(positiveSet.get(0));
                    positiveSet.remove(0);
                }
            }
            else{
                if(negativeSet.size() > 0){
                    mergeSet.add(negativeSet.get(0));
                    negativeSet.remove(0);
                }
            }
        }

        //for(int i=0;i<mergeSet.size();i++){
        //	System.out.println(mergeSet.get(i).getPolarity());
        //}

        ArrayList<node> connectedNodes = connectNodes(mergeSet);
        node root = nodes.get(0);
        return root;
    }

    private ArrayList<node> connectNodes(ArrayList<node> nodes){
        //connects the parent-child structure of the arraylist

        for(int i=0;i<(nodes.size()/2)-1;i++){
            node currNode = nodes.get(i);
            ArrayList<node> prevLeft = new ArrayList<node>();
            ArrayList<node> prevRight = new ArrayList<node>();

            currNode.setParent(nodes.get((i-1)/2));
            prevRight.add(nodes.get((2*i)+1));
            prevLeft.add(nodes.get(2*(i+1)));
            currNode.setLeft(prevLeft);
            currNode.setRight(prevRight);

        }

        //for (node node : nodes) {
          //  if (node.getLeft().size() != 0 && node.getRight().size() != 0) {
            //    System.out.println("NODE : " + node.getContents() + " POLARITY : " + node.getPolarity() + " LEFT : " + node.getLeft().get(0).getContents() + " RIGHT : " + node.getRight().get(0).getContents());
            //}
        //}

        globalRoot = nodes.get(0);

        return nodes;
    }

    private double calculateEntropy(int count) {
        double entropy = 0;
        double probability = Math.abs(count);
        entropy += -probability * (Math.log(probability)/Math.log(2));
        return entropy;
    }

    //dont call this until loadTrain has finished
    public node getGlobalRoot(){
        return globalRoot;
    }
}
