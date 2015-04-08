package com.asmith.decidrr;

import java.util.*;
public class node {
    private int polarity;
    private final String contents;
    private node parent;
    private ArrayList<node> left;
    private ArrayList<node> right;
    private double entropy;

    node(int polarity, String contents, double entropy){
        setParent(null);
        setLeft(new ArrayList<node>());
        setRight(new ArrayList<node>());
        this.entropy = entropy;
        this.polarity = polarity;
        this.contents = contents;
    }

    public node getParent(){
        return parent;
    }
    public int getPolarity(){
        return polarity;
    }

    public void setLeft(ArrayList<node> left){
        this.left = left;
    }

    public void setRight(ArrayList<node> right){
        this.right = right;
    }

    public ArrayList<node> getLeft(){
        return left;
    }

    public ArrayList<node> getRight(){
        return right;
    }

    public String getContents(){
        return contents;
    }
    public void setParent(node parent){
        this.parent = parent;
    }

    public double getEntropy(){
        return entropy;
    }

}