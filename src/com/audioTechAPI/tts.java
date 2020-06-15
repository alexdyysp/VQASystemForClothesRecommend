package com.audioTechAPI;

import com.audioTechAPI.play.Playpcm;
import com.audioTechAPI.voice2pcmwebapi.Voice2pcm;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

public class tts implements Runnable {
    private String text;

    public Voice2pcm voice2pcm;

    public Playpcm playpcm;

    public tts(String text, Voice2pcm voice2pcm, Playpcm playpcm){
        this.text = text;
        this.voice2pcm = voice2pcm;
        this.playpcm = playpcm;
    }

    @SneakyThrows
    @Override
    public void run(){
        voice2pcm.setText(text);

        String path = voice2pcm.transform();
        TimeUnit.SECONDS.sleep(1);

        System.out.println(path);
        try{
            playpcm.setFilepath(path);
            playpcm.playpcmbypath();
        }catch (Exception e){
            System.out.println(playpcm.getFilepath() + "play pcm error");
        }
    }
}
