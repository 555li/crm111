package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.Map;

public interface ClueService {
    int saveCreateClue(Clue clue);
    Clue queryClueForDetailById(String id);
    void saveConvertClue(Map<String,Object> map);
}
