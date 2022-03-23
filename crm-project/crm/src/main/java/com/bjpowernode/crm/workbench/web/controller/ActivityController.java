package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFUtils;
import com.bjpowernode.crm.commons.utils.UUIDutils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.sun.xml.internal.ws.spi.db.DatabindingException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //调用servlet层方法,查询所有的用户
        List<User> userList=userService.queryAllUsers();
        //把数据保存到request
        request.setAttribute("userList",userList);
        //请求转发到市场活动的主页面
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public @ResponseBody  Object saveCreateActivity(Activity activity, HttpSession session){
        User user =(User)session.getAttribute(Contants.SESSION_USER);
        //封装参数
        activity.setId(UUIDutils.getUUID());
        activity.setCreateTime(DateUtils.formateDateTime(new Date()));
        activity.setCreateBy(user.getId());
        ReturnObject returnObject=new ReturnObject();
        try { //调用service层方法
            int ret= 0;
            ret = activityService.saveCreateActivity(activity);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityByCondtionForPage.do")
    public @ResponseBody Object queryActivityConditionForPage(String name,String owner,String startDate,String endDate,int pageNo,int pageSize){
        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        //调用service层方法，查询数据
        List<Activity> activityList=activityService.queryActivityByConditionForPage(map);
        int  totalRows= activityService.queryCountOfActivityByCondition(map);
        Map<String,Object> retMap=new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);
        return retMap;

    }

    @RequestMapping("/workbench/activity/deleteActivityIds.do")
    public @ResponseBody Object deleteActivityIds(String[] id)
    {//调用service层方法，删除市场活动
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret=activityService.deleteActivityByIds(id);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/activity/queryActivityById.do")
    public @ResponseBody Object queryActivityById(String id){
        //调用service层方法,查询市场活动
        Activity activity=activityService.queryActivityById(id);
        //根据查询结果，返回响应信息
        return activity;

    }
    @RequestMapping("/workbench/activity/saveEditActivity.do")
    public @ResponseBody Object saveEditActivity(Activity activity,HttpSession session){
        //封装参数
        User user=(User) session.getAttribute((Contants.SESSION_USER));
        activity.setEditBy(user.getId());
        activity.setEditTime(DateUtils.formateDateTime(new Date()));

        ReturnObject returnObject=new ReturnObject();
        try {
            int ret= activityService.saveEditActivity(activity);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重拾...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后重拾...");
        }return returnObject;
    }
    @RequestMapping("workbench/activity/exportAllActivitys.do")
    public void exportAllActivitys(HttpServletResponse response) throws IOException {
        //调用service层方法,查询所有的市场活动
        List<Activity> activityList=activityService.queryAllActivitys();
        //创建excel文件,并且把activityList写入到excel文件
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet=wb.createSheet("市场活动列表");
        HSSFRow row=sheet.createRow(0);
        HSSFCell cell=row.createCell(0);
        cell.setCellValue("ID");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改者");

        //遍历activityList,创建HSSFRow对象，生成所有的数据行
        if(activityList!=null&&activityList.size()>0){
            Activity activity=null;
            for(int i=0;i<activityList.size();i++){
                activity=activityList.get(i);
                //没遍历出一个activity,生成一行
                row=sheet.createRow(i+1);
                cell=row.createCell(0);
                cell.setCellValue(activity.getId());
                cell=row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell=row.createCell(2);
                cell.setCellValue(activity.getName());
                cell=row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell=row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell=row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell=row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell=row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell=row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell=row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell=row.createCell(10);
                cell.setCellValue(activity.getEditBy());

            }
        }
        //根据web对象生成excel文件
//        OutputStream os=new FileOutputStream("D:\\Excel\\activityList.xls");
//        wb.write(os);
//        //关闭资源
//        os.close();
//        wb.close();

        //把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        OutputStream out = response.getOutputStream();
//        InputStream is=new FileInputStream("D:\\Excel\\activityList.xls");
//        byte[] buff=new byte[256];
//        int len=0;
//        while((len=is.read(buff))!=-1){
////            out.write(buff,0,len);
////        }
//        is.close();
        wb.write(out);
        wb.close();
        out.flush();

    }
    @RequestMapping("/workbench/activity/importActivity.do")
    public @ResponseBody Object importActivity(MultipartFile activityFile,HttpSession session,String userName){
        ReturnObject returnObject=new ReturnObject();
        User user= (User)  session.getAttribute(Contants.SESSION_USER);
        //把接收到的文件写入到磁盘
        try {
//            String orginalFilename=activityFile.getOriginalFilename();
//            File file=new File("D:\\Excel\\"+orginalFilename);
//            activityFile.transferTo(file);
            //解析Excel文件，获取文件中的数据，并且封装成activityList
//            InputStream is=new FileInputStream("D:\\Excel\\"+orginalFilename);
            InputStream is=activityFile.getInputStream();
            HSSFWorkbook wb=new HSSFWorkbook(is);
            HSSFSheet sheet=wb.getSheetAt(0);
            List<Activity> activityList=new ArrayList<>();
            HSSFRow row=null;
            HSSFCell cell=null;
            Activity activity =null;
            for(int i=1;i<=sheet.getLastRowNum();i++)
            {
                row= sheet.getRow(i);
                activity = new Activity();
                activity.setId(UUIDutils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateBy(user.getId());
                activity.setCreateTime(DateUtils.formateDateTime(new Date()));

                for(int j=0;j<row.getLastCellNum();j++){
                    cell=row.getCell(j);
                    String cellValue= HSSFUtils.getCellValueForStr(cell);

                    if(j==0){
                        activity.setName(cellValue);
                    }else if(j==1){
                        activity.setStartDate(cellValue);
                    }else if(j==2){
                        activity.setEndDate(cellValue);
                    }else if(j==3){
                        activity.setCost(cellValue);
                    }else if(j==4){
                        activity.setDescription(cellValue);
                    }
                }
                //每一行中所有列都封装完成后，把activity保存到list中
                activityList.add(activity);
            }
            //调用service层方法，保存市场活动
            int ret=activityService.saveCreateActivityByList(activityList);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetDate(ret);
        } catch (IOException e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请重试...");
        }return returnObject;

    }
        @RequestMapping("workbench/activity/detailActivity.do")
      public String detailActivity(String id,HttpServletRequest request){
        //调用service层方法，查询数据
          Activity activity=activityService.queryActivityForDetailById(id);
          List<ActivityRemark> remarkList=activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
          request.setAttribute("activity",activity);
          request.setAttribute("remarkList",remarkList);
          return "workbench/activity/detail";

      }
}
