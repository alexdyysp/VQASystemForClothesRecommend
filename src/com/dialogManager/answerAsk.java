package com.dialogManager;

import java.util.HashMap;

public interface answerAsk {
    public String recAns(HashMap<String, String> attr);

    public String collectAns(HashMap<String, String> attr);

    public String defaultAns();
}
