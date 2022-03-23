package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChartController {
    @Autowired
    private TranService tranService;
    @RequestMapping("/workbench/chart/transaction/index.do")
    public  String index(){
        return "workbench/chart/transaction/index";
    }
    @RequestMapping("/workbench/chart/transaction/queryCountOfTranGroupByStage.do")
    public @ResponseBody
    Object queryCountOfTranGroupByStage(){
        List<FunnerlVO> funnerlVOList=tranService.queryCountOfTranGroupByStage();
        return funnerlVOList;
    }



}
