package com.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TextConfig {
    public Map<String, ArrayList<String>> reco = new HashMap<String,ArrayList<String>>();
    public Map<String, String> info = new HashMap<String, String>();

    public TextConfig(){
        String str = "";
        // RecText
        try{
            BufferedReader br = new BufferedReader(new FileReader("D:\\1111.txt"));
            str = br.readLine();
            while(str != null){
                String[] text = str.split(":");
                //StringBuffer sss = new StringBuffer();

                if(reco.get(text[0])==null){
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add(text[1]);
                    reco.put(text[0], tmp);

                }else{
                    //sss.append(reco.get(text[0]));
                    //sss.append(text[1]);
                    reco.get(text[0]).add(text[1]);
                }

                str = br.readLine();

            }
        }catch (IOException e){
            e.printStackTrace();
        }

        // InfoText
        try{
            BufferedReader br = new BufferedReader(new FileReader("D:\\2222.txt"));
            str = br.readLine();
            while(str != null){
                String[] text = str.split(":");
                //StringBuffer sss = new StringBuffer();

                if(info.get(text[0])==null){
                    info.put(text[0], text[1]);
                }

                str = br.readLine();

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getRec(String key,int index){
        String result = "null";
        if(reco.get(key)==null){
            return "没有此类推荐，请等待后续内容...";
        }else{
            if(index>=reco.get(key).size()){
                return "推荐到底了...";
            }else{
                return reco.get(key).get(index);
            }
        }
        //return result;
    }

    public String getInfo(String key){
        String result = "null";
        if(info.get(key)==null){
            return "没有该类服装关键词词信息，请等待后续内容...";
        }else{
            return info.get(key);
        }
        //return result;
    }


    public static void main(String[] args) {
        TextConfig tc = new TextConfig();
        //tc.read();
        System.out.println(tc.reco.keySet().contains("大胸"));
        System.out.println(tc.getRec("",0));
        String key = "鹅蛋脸";
        System.out.println(key + tc.getRec(key,0));
        System.out.println(key + tc.getRec(key,1));

        key = "直筒型";
        System.out.println(key + tc.getRec(key,0));
        System.out.println(key + tc.getRec(key,1));

        key = "直筒裙";
        System.out.println(key + tc.getInfo(key));
    }

}
