package com.graphSearch.service.serviceimpl;

import com.graphSearch.repository.Repository;
import com.graphSearch.service.Account_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class AccountServiceImpl implements Account_Service {
    @Autowired
    public Repository dao;
    public List<String> HandleCheck(String str){
        String[] text = str.split(",");
        List<String> list = new LinkedList<>();
        if(text[0].equals("null"))
        {
            list.add(text[0]);
            return list;
        }
        for(int i=0;i<text.length;i++)
        {
            list.add(text[i]);

        }
        return list;
    }

    public List<String> GetRecoString(List<String> str){
        List<String> Recoo = new LinkedList<>();
        if(str.get(0).equals("null"))
        {
            Recoo.add("未选择相关内容。");
            return Recoo;
        }
        for(int i=0;i<str.size();i++)
        {

            Recoo.add(dao.findByName(str.get(i)).getRecommend());
        }
        return Recoo;
    }

    public String deal(String str){
        String[] test = str.split(",");
        if(test[0].equals("null")){
            return test[0];
        }
        return str;
    }

}
