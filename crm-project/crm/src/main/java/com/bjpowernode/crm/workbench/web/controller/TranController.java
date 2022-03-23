package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TranController {
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TranService tranService;

    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request){
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transaction");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        //请求转发
        return "workbench/transaction/index";

    }
    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(String id,HttpServletRequest request){
        List<User> userList = userService.queryAllUsers();
        List<DicValue> transactionTypeList= dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("userList",userList);

        return "workbench/transaction/save";


    }
    @RequestMapping("/workbench/transaction/getPossiblityByStage.do")
    public @ResponseBody Object getPossiblityByStage(String stageValue){
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String posibility = bundle.getString(stageValue);
        //封窗参数
        return posibility;
    }
    @RequestMapping("/workbench/transaction/queryCustomerNameByName.do")
    public @ResponseBody Object queryCustomerNameByName(String userName){
        List<String> customerNameList= customerService.queryAllCustomerName(userName);
        return customerNameList;
    }
    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    public @ResponseBody Object saveCreateTran(@RequestParam  Map<String,Object> map, HttpSession session){
        map.put(Contants.SESSION_USER,session.getAttribute(Contants.SESSION_USER));
            ReturnObject returnObject=new ReturnObject();
            //调用service方法
            try {
                tranService.saveCreateTran(map);
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重试...");
            }
            return returnObject;
    }
}
