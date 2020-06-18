package com.audioTechAPI;

import com.audioTechAPI.play.Playpcm;

import java.util.concurrent.TimeUnit;

public class playByPath extends Thread{
    public String filepath = "null";
    public Playpcm playpcm;

    public playByPath(String filepath, Playpcm playpcm){
        this.filepath = filepath;
        this.playpcm = playpcm;

    }

    @Override
    public void run(){

        try{
            playpcm.setFilepath(filepath);
            playpcm.playpcmbypath();
        }catch (Exception e){
            System.out.println(playpcm.getFilepath() + "play pcm error");
        }
    }
}
