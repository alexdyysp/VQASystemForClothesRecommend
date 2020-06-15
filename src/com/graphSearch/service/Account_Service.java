package com.graphSearch.service;

import java.util.List;

public interface Account_Service {
    //public void SelectAll();
    public List<String> HandleCheck(String str);
    public List<String> GetRecoString(List<String> str);
    public String deal(String str);
}
