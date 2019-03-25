package cn.appsys.service.developer;

import java.util.List;


import cn.appsys.pojo.AppInfo;

public interface AppInfoService {
	
	//分条件查询App列表
	List<AppInfo> getAppInfoList(String querySoftwareName,Integer queryStatus,
			Integer queryCategoryLevel1,Integer queryCategoryLevel2,
			Integer queryCategoryLevel3,Integer queryFlatformId,
			Integer currentPageNo,Integer pageSize);
	
	//获取总数量
	int getAppInfoCount(String querySoftwareName,Integer queryStatus,Integer queryCategoryLevel1,
			   Integer queryCategoryLevel2,Integer queryCategoryLevel3,Integer queryFlatformId);
	//新增App
	boolean addAppInfo(AppInfo appInfo);
	//判断重名
	AppInfo checkName(String APKName);
	//获取详细信息
	AppInfo getAppInfo(Integer id);
	//修改信息
	boolean modify(AppInfo appInfo);
	//删除图片
	int delPic(int id);
	String getAPKName(int id); 
	boolean UpdateStatus(AppInfo appInfo);
	boolean deleteAppInfoById(Integer delId);
}
