package com.dialogManager;

import com.huaban.analysis.jieba.JiebaSegmenter;

import java.util.List;

public class IentionRecognize {
    /* 意图类别清单
        编号	意图	分类标签
    0	请求推荐服装	rec_clothes
    1	提供用户信息	offer_info
    2	拒接推荐结果	change_rec
    3	接受推荐结果	accept_rec
    4	感谢再见	    Goodbye
    5   听不清          notRec
    6   查询服关键词    require_info
     */
    public static int state = 0;
    public static String[] IentionState = {"请求推荐服装","提供用户信息","拒接推荐结果","接受推荐结果","感谢再见","听不清","查询服装关键词"};

    public static String[] State0Word = {"推荐","开始"};
    public static String[] State1Word = {"身材","脸型","皮肤","体型"};
    public static String[] State2Word = {"不","不喜欢","换一个","换","下一个","一个","下"};
    public static String[] State3Word = {"好的","可以","这个好","谢谢","不错"};
    public static String[] State4Word = {"谢谢","感谢","拜拜","再见"};
    public static String[] State6Word = {"是什么","东西","什么","查一下","意思"};

    public static String[] AttrWord = {};

    public static int intentionRec(String[] input){
        switch(includewhat(input)){
            case 0:
                state = 0;
                break;
            case 1:
                state = 1;
                break;
            case 2:
                state = 2;
                break;
            case 3:
                state = 3;
                break;
            case 4:
                state = 4;
                break;
            case 5:
                state = 5;
                break;
            case 6:
                state = 6;
                break;
        }
        return state;
    }

    public static int includewhat(String[] input){
        if(ruleBaseMatch(input, State0Word)==true) return 0;
        if(ruleBaseMatch(input, State1Word)==true) return 1;
        if(ruleBaseMatch(input, State2Word)==true) return 2;
        if(ruleBaseMatch(input, State3Word)==true) return 3;
        if(ruleBaseMatch(input, State4Word)==true) return 4;
        if(ruleBaseMatch(input, State6Word)==true) return 6;
        return 5;
    }

    public static boolean ruleBaseMatch(String[] input, String[] term){
        for(int i=0; i<input.length; i++){
            for(int j=0; j<term.length; j++){
                //System.out.println(input[i] + "=>" + term[j]);
                if(input[i].equals(term[j])){
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args){
        IentionRecognize ientionRecognize = new IentionRecognize();
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> input = segmenter.sentenceProcess("我是直筒型身材");
        String[] arr = input.toArray(new String[input.size()]);
        System.out.println(includewhat(arr));
        System.out.println(IentionRecognize.IentionState[intentionRec(arr)]);
    }
}
