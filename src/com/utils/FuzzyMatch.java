package com.utils;

import org.apache.commons.text.similarity.FuzzyScore;

import java.util.ArrayList;

import static java.util.Locale.CHINESE;

public class FuzzyMatch {

    FuzzyScore fuzzyScore = null;

    public FuzzyMatch(){
        FuzzyScore fuzzyScore = new FuzzyScore(CHINESE);
    }

    public static void main(String[] args){
        FuzzyScore fuzzyScore = new FuzzyScore(CHINESE);
        //System.out.println(fuzzyScore.fuzzyScore("推荐衣服","推荐"));
        //FuzzyScore fuzzyScore = new FuzzyScore(Locale.ENGLISH);
        String[] array = {"推荐衣服","换衣服","一件","随便"};
        String query = "随便推荐一件衣服";
        Integer queryScore = fuzzyScore.fuzzyScore(query, query);
        ArrayList<String> arrayList = new ArrayList<>();
        for (String s : array) {
            Integer score = fuzzyScore.fuzzyScore(s,query);
            System.out.println(score);
            if (score>=queryScore){
                arrayList.add(s);
            }
        }
        System.out.println(arrayList.toString());
    }


}
