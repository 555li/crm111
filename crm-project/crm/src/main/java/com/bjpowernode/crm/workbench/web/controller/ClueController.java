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
        //????????????
        return "workbench/clue/index";

    }
    @RequestMapping("/workbench/clue/saveCreateClue.do")
    public @ResponseBody Object saveCreateClue(Clue clue, HttpSession session){
        User user =(User)session.getAttribute(Contants.SESSION_USER);
        //????????????
        clue.setId(UUIDutils.getUUID());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));
        clue.setCreateBy(user.getId());
        ReturnObject returnObject=new ReturnObject();
        try { //??????service?????????
            int ret= 0;
            ret = clueService.saveCreateClue(clue);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("?????????,???????????????...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("?????????,???????????????...");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,HttpServletRequest request){
      Clue clue=clueService.queryClueForDetailById(id);
      List<ClueRemark> remarkList=clueRemarkService.queryClueRemarkForDetailByClueId(id);
      List<Activity> activityList=activityService.queryActivityForDetailByClueId(id);
      //ba???????????????request???
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
       //????????????
        ClueActivityRelation car =null;
        List<ClueActivityRelation> relationList=new ArrayList<>();
        for(String ai:activityId ){
             car = new ClueActivityRelation();
             car.setActivityId(ai);
             car.setClueId(clueId);
             car.setId(UUIDutils.getUUID());
             relationList.add(car);
        }
        //??????serviec?????????????????????????????????????????????????????????
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret= clueActivityRelationService.saveCreateClueActivityRelationByList(relationList);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetDate(activityList);

            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("?????????,???????????????...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("?????????,???????????????...");
        }return returnObject;
    }
    @RequestMapping("/workbench/clue/saveUnBund.do")
    public @ResponseBody Object saveUnBund(ClueActivityRelation relation){
        //????????????
        //??????serviec?????????????????????????????????????????????????????????
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret= clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("?????????,???????????????...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("?????????,???????????????...");
        }return returnObject;
    }
    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        //??????service????????????????????????????????????
        Clue clue=clueService.queryClueForDetailById(id);
        //??????????????????request???
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stageList);
        return "workbench/clue/convert";


    }
    @RequestMapping("/workbench/clue/queryActivityForConvertByNameClueId.do")
    public @ResponseBody Object queryActivityForConvertByNameClueId(String activityName,String clueId){
        //????????????
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);

        List<Activity> activityList = activityService.queryActivityForConvertByNameClueId(map);
        return activityList;

    }
    @RequestMapping("/workbench/clue/convertClue.do")
    public @ResponseBody Object convertClue(String money,String clueId,String name,String expectedDate,String stage,String activity_id,String isCreateTran,HttpSession session){
        //????????????
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
        //??????service??????
        try {
            clueService.saveConvertClue(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("?????????,???????????????...");
        }
        return returnObject;

    }

}
