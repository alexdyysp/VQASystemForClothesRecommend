package com.utils;

import java.io.*;

public class WriteTxt {

    private String path = "D:\\DHU\\clothRec\\iat_ws_js_demo\\src\\resources";
    private String filename = "log.txt";

    public WriteTxt() throws IOException {
        File writename = new File(path + "\\" + filename);// 相对路径，如果没有则要建立一个新的output。txt文件
        if(writename.exists()){
            writename.delete();
            FileWriter writenameW = new FileWriter(writename, true);
        }else{
            FileWriter writenameW = new FileWriter(writename, true);
        }

    }

    public void write2TXT(String content){
        FileWriter fw = null;
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            File f=new File(path + "\\" + filename);
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(content);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        WriteTxt writeTxt = new WriteTxt();
        writeTxt.write2TXT("assfewadd");
    }
}
