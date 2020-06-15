package com.web;

import com.audioTechAPI.play.Playpcm;
import com.audioTechAPI.voice2pcmwebapi.Voice2pcm;
import com.dialogManager.QASystem;
import com.utils.WriteTxt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

//@SpringBootApplication
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class DyTest {
    public static void main(String[] args) throws IOException {

        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");

        SpringApplication.run(DyTest.class ,args);
    }
}
