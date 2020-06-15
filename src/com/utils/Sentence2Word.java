package com.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.WordDictionary;


public class Sentence2Word {

    public JiebaSegmenter segmenter = new JiebaSegmenter();
    String sentences = "北京京天威科技发展有限公司大庆车务段的装车数量";

    /**
     * 读取conf目录下所有的自定义词库**.dict文件。
     */

    public Sentence2Word(){
        Path path = Paths.get(new File(getClass().getClassLoader().getResource("dicts/clotheword.dict").getPath()).getAbsolutePath());
        WordDictionary.getInstance().loadUserDict(path);
    }

    /*
    @Override
    protected void setUp() throws Exception {
        WordDictionary.getInstance().init(Paths.get("conf"));
    }
    */

    public static void main(String[] args) {
        /*单词*/
        Sentence2Word sentence2Word = new Sentence2Word();
        //JiebaSegmenter segmenter = new JiebaSegmenter();
        //Path path = Paths.get(new File(getClass().getClassLoader().getResource("dicts/clotheword.dict").getPath() ).getAbsolutePath());
        //WordDictionary.getInstance().loadUserDict(path);
        System.out.println(sentence2Word.segmenter.sentenceProcess("我是直筒型身材"));
        System.out.println(sentence2Word.segmenter.sentenceProcess("随便推荐一件衣服"));
        System.out.println(sentence2Word.segmenter.sentenceProcess("我脸小"));
        System.out.println(sentence2Word.segmenter.sentenceProcess("皮肤偏黄"));
        System.out.println(sentence2Word.segmenter.sentenceProcess("不喜欢这个想换一个"));
        System.out.println(sentence2Word.segmenter.sentenceProcess("帮我查一下直筒裙是什么意思"));
        System.out.println(sentence2Word.segmenter.sentenceProcess("什么是蝙蝠袖"));

    }
}
