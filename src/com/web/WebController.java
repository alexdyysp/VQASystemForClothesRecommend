package com.web;

import com.audioTechAPI.play.Playpcm;
import com.audioTechAPI.playByPath;
import com.audioTechAPI.tts;
import com.audioTechAPI.voice2pcmwebapi.Voice2pcm;
import com.dialogManager.QASystem;
import com.utils.WriteTxt;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/test")
public class WebController {

    ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");

    @PostMapping("/result")
    public String getResult(@RequestParam(name="result") String data,Model model) throws Exception {
        System.out.println("askFromUser = " + data);

        //TextConfig textConfig = ac.getBean("TextConfig", TextConfig.class);
        QASystem qaSystem = ac.getBean("QASystem", QASystem.class);
        Playpcm playpcm = ac.getBean("Playpcm", Playpcm.class);
        Voice2pcm voice2pcm = ac.getBean("Voice2pcm", Voice2pcm.class);

        String output = qaSystem.dialoge(data);
        /* TTS */
        new Thread(new tts(output, voice2pcm, playpcm)).start();

        System.out.println("/result : " + output);
        WriteTxt writeTxt = new WriteTxt();
        writeTxt.write2TXT("用户：" + data);
        writeTxt.write2TXT("系统：" + output);
        model.addAttribute("answer", output);
       // return ResponseEntity.ok("");
        return "index";
    }


    @PostMapping("/answer")
    public String answer2User(@RequestParam(name="answer") String data, Model model){
        //DM dm = new DM();
        //String output = dm.dialoge(data);
        //System.out.println("/answer"+output);

        model.addAttribute("answer", "测试按钮一下");

        return "index";
    }

    @PostMapping("/jump2index")
    public void playBackground(){
        Playpcm playpcm = ac.getBean("Playpcm", Playpcm.class);
        String filepath = "D:\\DHU\\clothRec\\iat_ws_js_demo\\src\\resources\\tts\\helloaudio.pcm";
        //String output = "欢迎光临，请问有什么可以帮您的嘛";
        /* TTS */
        new Thread(new playByPath(filepath = filepath, playpcm = playpcm)).start();
    }

}
