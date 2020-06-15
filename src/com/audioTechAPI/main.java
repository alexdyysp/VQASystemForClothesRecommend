package com.audioTechAPI;

import com.audioTechAPI.play.Playpcm;
import com.audioTechAPI.voice2pcmwebapi.Voice2pcm;

import java.util.concurrent.TimeUnit;

// 959806114114

public class main {
    public static void main(String[] args) throws Exception {
        Playpcm playpcm = new Playpcm();
        Voice2pcm voice2pcm = new Voice2pcm();

        voice2pcm.setText("随便试一下这件衣服吧");
        System.out.println(voice2pcm.getText());

        String path = voice2pcm.transform();
        TimeUnit.SECONDS.sleep(2);

        System.out.println(path);
        try{
            playpcm.setFilepath(path);
            System.out.println(playpcm.getFilepath());
            playpcm.playpcmbypath();
        }catch (Exception e){
            System.out.println(playpcm.getFilepath() + "play pcm error");
        }
    }
}
