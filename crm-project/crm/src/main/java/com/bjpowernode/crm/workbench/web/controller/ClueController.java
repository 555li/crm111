package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDutils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.ClueMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ClueService clueService;
    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;


    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
      List<User> userList=userService.queryAllUsers();
      List<DicValue> applicationList=dicValueService.queryDicValueByTypeCode("application");
      List<DicValue> clueStateList=dicValueService.queryDicValueByTypeCode("clueState");
      List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
       request.setAttribute("userList",userList);
        request.setAttribute("applicationList",applicationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        //请求转发
        return "workbench/clue/index";

    }
    @RequestMapping("/workbench/clue/saveCreateClue.do")
    public @ResponseBody Object saveCreateClue(Clue clue, HttpSession session){
        User user =(User)session.getAttribute(Contants.SESSION_USER);
        //封装参数
        clue.setId(UUIDutils.getUUID());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));
        clue.setCreateBy(user.getId());
        ReturnObject returnObject=new ReturnObject();
        try { //调用service层方法
            int ret= 0;
            ret = clueService.saveCreateClue(clue);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后重试...");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,HttpServletRequest request){
      Clue clue=clueService.queryClueForDetailById(id);
      List<ClueRemark> remarkList=clueRemarkService.queryClueRemarkForDetailByClueId(id);
      List<Activity> activityList=activityService.queryActivityForDetailByClueId(id);
      //ba数据保存到request中
        request.setAttribute("clue",clue);
        request.setAttribute("remarkList",remarkList);
        request.setAttribute("activityList",activityList);
        return "workbench/clue/detail";


    }
    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    public @ResponseBody Object queryActivityForDetailByNameClueId(String activityName,String clueId){
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> activityList=activityService.queryActivityForDetailByNameClueId(map);
        return activityList;
    }
    @RequestMapping("/workbench/clue/saveBund.do")
    public @ResponseBody Object saveBund(String[] activityId,String clueId){
       //封窗参数
        ClueActivityRelation car =null;
        List<ClueActivityRelation> relationList=new ArrayList<>();
        for(String ai:activityId ){
             car = new ClueActivityRelation();
             car.setActivityId(ai);
             car.setClueId(clueId);
             car.setId(UUIDutils.getUUID());
             relationList.add(car);
        }
        //调用serviec方法，批量保存线索和市场活动的关联关系
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret= clueActivityRelationService.saveCreateClueActivityRelationByList(relationList);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetDate(activityList);

            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后重试...");
        }return returnObject;
    }
    @RequestMapping("/workbench/clue/saveUnBund.do")
    public @ResponseBody Object saveUnBund(ClueActivityRelation relation){
        //封窗参数
        //调用serviec方法，批量保存线索和市场活动的关联关系
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret= clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后重试...");
        }return returnObject;
    }
    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        //调用service方法，查询线索的明细信息
        Clue clue=clueService.queryClueForDetailById(id);
        //把数据保存到request中
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stageList);
        return "workbench/clue/convert";


    }
    @RequestMapping("/workbench/clue/queryActivityForConvertByNameClueId.do")
    public @ResponseBody Object queryActivityForConvertByNameClueId(String activityName,String clueId){
        //封窗参数
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);

        List<Activity> activityList = activityService.queryActivityForConvertByNameClueId(map);
        return activityList;

    }
    @RequestMapping("/workbench/clue/convertClue.do")
    public @ResponseBody Object convertClue(String money,String clueId,String name,String expectedDate,String stage,String activity_id,String isCreateTran,HttpSession session){
        //封窗参数
        Map<String,Object> map=new HashMap<>();
        map.put("money",money);
        map.put("clueId",clueId);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activity_id",activity_id);
        map.put("isCreateTran",isCreateTran);
        map.put(Contants.SESSION_USER,session.getAttribute(Contants.SESSION_USER));
        ReturnObject returnObject=new ReturnObject();
        //调用service方法
        try {
            clueService.saveConvertClue(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后重试...");
        }
        return returnObject;

    }

}
