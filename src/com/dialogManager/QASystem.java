package com.dialogManager;

import com.utils.Sentence2Word;
import com.utils.TextConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.dialogManager.IentionRecognize.IentionState;

public class QASystem {
    public static int count = 0; //对话轮数
    public int DS_last = -1; // 上一轮对话状态
    public int DS = 0; //对话状态
    static IentionRecognize ientionRecognize = new IentionRecognize();
    public HashMap<String, String> attr = new HashMap<>();
    public ArrayList<String> userAttr = new ArrayList<>();
    public HashSet<String> attrarry = new HashSet<>();
    public TextConfig textConfig;
    public Sentence2Word sentence2Word;

    public QASystem(TextConfig textConfig, Sentence2Word sentence2Word){
        this.textConfig = textConfig;
        this.sentence2Word = sentence2Word;
        attr.put("脸型",null);
        attr.put("身材",null);
        attr.put("体型",null);
        attr.put("肤色",null);
        String str = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader("D:\\DHU\\clothRec\\iat_ws_js_demo\\src\\resources\\hotwords.txt"));
            str = br.readLine();
            while(str != null){
                attrarry.add(str);
                str = br.readLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String dialoge(String user_input){
        //JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> input = sentence2Word.segmenter.sentenceProcess(user_input);
        String[] arr = input.toArray(new String[input.size()]);

        /*
        for(int i=0; i<arr.length; i++){
            System.out.print(arr[i]+" ");
        }
        System.out.println();
        */

        this.DS = ientionRecognize.intentionRec(arr);
        System.out.println("意图识别结果：" + IentionState[this.DS]);

        if(this.DS == 0){
            count++;
            this.DS_last = DS;
            return Answer2User.askAttrPattern0;
        }

        if(this.DS == 1){
            count++;
            // 填充属性槽
            // 如果有属性词，userAttr
            for(int i=0; i<arr.length; i++){
                if(attrarry.contains(arr[i])){
                    userAttr.add(arr[i]);
                }
            }
            // 返回推荐结果
            this.DS_last = DS;
            return Answer2User.clotheRecommand2user(userAttr, textConfig,0);
        }

        if(this.DS == 2){
            count++;
            // 填充属性槽
            // 如果有属性词，userAttr
            for(int i=0; i<arr.length; i++){
                if(attrarry.contains(arr[i])){
                    userAttr.add(arr[i]);
                }
            }
            // 返回推荐结果
            this.DS_last = DS;
            return Answer2User.clotheRecommand2user(userAttr, textConfig,1);
        }

        if(this.DS == 4 || this.DS == 3){
            count++;

            return Answer2User.Pattern6;
        }

        if(this.DS == 6){
            for(int i=0; i<arr.length; i++){
                if(textConfig.info.containsKey(arr[i]))
                    return Answer2User.clotheInfo2user(textConfig, arr[i]);
            }
        }

        this.DS_last = 5;
        return "没听清，请再说一遍...";
    }

    public static void main(String[] args){
        TextConfig textConfig = new TextConfig();
        Sentence2Word sentence2Word = new Sentence2Word();
        QASystem qasystem = new QASystem(textConfig,sentence2Word);
        //System.out.println(dm.attrarry);
        System.out.println(qasystem.dialoge("推荐一件衣服吧"));
        System.out.println(qasystem.dialoge("我是直筒型身材"));
        System.out.println(qasystem.dialoge("谢谢"));
        System.out.println(qasystem.dialoge("帮我查一下直筒裙是什么意思"));
    }
}
