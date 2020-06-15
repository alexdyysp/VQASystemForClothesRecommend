package com.graphSearch.controller;

import com.graphSearch.domain.Entity.Hair;
import com.graphSearch.domain.Node;
import com.graphSearch.repository.Repository;
import com.graphSearch.service.Account_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class AccountController {
    @Autowired
    Repository dao;
    @Autowired
    Account_Service as;
    @RequestMapping(value = "/login")
    public String toLogin() {return "login";}
    @RequestMapping(value = "/index")
    public String toIndex() {return "index";}
    @RequestMapping(value = "/register")
    public String toRegister() {return "register";}
    @RequestMapping(value = "/recommend")
    public String toRecommend(){return "recommend";}
    @RequestMapping(value = "/recommen")
    public String toRecommen(){return "recommen";}



    @RequestMapping("/add")
    public String add(){
        Node a = new Hair();
        a = dao.findByName("长直发");
        System.out.println(a.getRecommend());
        return a.getRecommend();
    }

    @PostMapping("/handlerecommend")

    public String handlerecommend(@RequestParam String figure,@RequestParam String face,@RequestParam String uphalf,
                                  @RequestParam String downhalf,@RequestParam String hair,@RequestParam String skin,
                                  @RequestParam String color, Model model){

        uphalf = as.deal(uphalf);
        downhalf = as.deal(downhalf);
        color = as.deal(color);
        List<String> listuphalf = as.HandleCheck(uphalf);
        List<String> listdownhalf = as.HandleCheck(downhalf);
        List<String> listcolor = as.HandleCheck(color);

        String figure_reco = "未选择对应内容";
        String face_reco= "未选择对应内容";
        String hair_reco= "未选择对应内容";
        String skin_reco= "未选择对应内容";


        if(!figure.equals("null"))
        {
            figure_reco = dao.findByName(figure).getRecommend();
        }else{
            figure = "暂未选择身材标签";
        }

        if(!face.equals("null"))
        {
            face_reco = dao.findByName(face).getRecommend();
        }else{
            face = "暂未选择脸型标签";
        }

        if(!hair.equals("null"))
        {
            hair_reco = dao.findByName(hair).getRecommend();
        }else{
            hair = "暂未选择发型标签";
        }
        if(!skin.equals("null"))
        {
            skin_reco = dao.findByName(skin).getRecommend();
        }else{
            skin = "暂未选择皮肤标签";
        }

        if(uphalf.equals("null"))
        {
            uphalf = "暂未选择上半躯干标签";
        }

        if(downhalf.equals("null"))
        {
            downhalf = "暂未选择上半躯干标签";
        }

        if(color.equals("null"))
        {
            color = "暂未选择上半躯干标签";
        }

        List<String> uphalf_reco = as.GetRecoString(listuphalf);
        List<String> downhalf_reco = as.GetRecoString(listdownhalf);
        List<String> color_reco = as.GetRecoString(listcolor);


        model.addAttribute("figureout",figure);
        model.addAttribute("faceout",face);
        model.addAttribute("uphalfout",uphalf);
        model.addAttribute("downhalfout",downhalf);
        model.addAttribute("hairout",hair);
        model.addAttribute("skinout",skin);
        model.addAttribute("colorout",color);


        model.addAttribute("figure_reco",figure_reco);
        model.addAttribute("face_reco",face_reco);
        model.addAttribute("uphalf_reco",uphalf_reco);
        model.addAttribute("downhalf_reco",downhalf_reco);
        model.addAttribute("hair_reco",hair_reco);
        model.addAttribute("skin_reco",skin_reco);
        model.addAttribute("color_reco",color_reco);

        return "result";
    }



}
