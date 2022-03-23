package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveCreateActivity(Activity activity);
    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);
    int queryCountOfActivityByCondition(Map<String,Object> map);

    int deleteActivityByIds(String[] ids);
    Activity queryActivityById(String id);
    int saveEditActivity(Activity activity);
    List<Activity> queryAllActivitys();
    int saveCreateActivityByList(List<Activity> activityList);
    Activity queryActivityForDetailById(String id);
    List<Activity> queryActivityForDetailByClueId(String clueId);
    List<Activity> queryActivityForDetailByNameClueId(Map<String,Object> map);
    List<Activity> queryActivityForDetailByIds(String[] ids);
    List<Activity> queryActivityForConvertByNameClueId(Map<String,Object> map);

}
