package com.dialogManager;

import com.utils.TextConfig;

import java.util.ArrayList;

public class Answer2User {

    public static String askAttrPattern0 = "你什么身材？";
    public static String askAttrPattern1 = "你什么体型？";
    public static String askAttrPattern2 = "你什么肤色？";
    public static String askAttrPattern3 = "你什么脸型？";

    public static String Pattern1 = "给你推荐xxxx";

    public static String Pattern5 = "没听清楚，请再说一遍";
    public static String Pattern6 = "感谢使用，欢迎再来";

     public static String clotheRecommand2user(ArrayList<String> attrUser, TextConfig tc,int index){
         //TextConfig tc = new TextConfig();
         String answer = "";
         for(int i=0; i<attrUser.size(); i++){
             answer += tc.getRec(attrUser.get(i), index);
         }
         return answer;
     }

    public static String clotheInfo2user(TextConfig tc, String key){
        //TextConfig tc = new TextConfig();
        String answer = "";
        answer = tc.getInfo(key);
        return answer;
    }
}
