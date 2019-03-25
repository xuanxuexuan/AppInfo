package cn.appsys.service.developer.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.developer.appInfo.AppInfoMapper;
import cn.appsys.dao.developer.appVersion.AppVersionMapper;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.service.developer.AppInfoService;

@Service
public class AppInfoServiceImpl implements AppInfoService{
	
	@Resource
	private AppInfoMapper appMapper;
	@Resource
	private AppVersionMapper appVersionMapper;
	@Override
	public List<AppInfo> getAppInfoList(String querySoftwareName,
			Integer queryStatus, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3,Integer queryFlatformId,Integer currentPageNo,
			Integer pageSize) {
		// TODO Auto-generated method stub
		return appMapper.getAppInfoList(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3,queryFlatformId, currentPageNo, pageSize);
	}
	@Override
	public int getAppInfoCount(String querySoftwareName, Integer queryStatus,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId) {
		// TODO Auto-generated method stub
		return appMapper.getAppInfoCount(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId);
	}
	@Override
	public boolean addAppInfo(AppInfo appInfo) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(appMapper.addAppInfo(appInfo) > 0){
			flag = true;
		}
		return flag; 
	}
	
	@Override
	public AppInfo getAppInfo(Integer id) {
		// TODO Auto-generated method stub
		return appMapper.getAppInfo(id);
	}
	@Override
	public AppInfo checkName(String APKName) {
		// TODO Auto-generated method stub
		return appMapper.checkName(APKName);
	}
	@Override
	public boolean modify(AppInfo appInfo) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(appMapper.modify(appInfo) > 0){
			flag = true;
		}
		return flag;
	}
	@Override
	public int delPic(int id) {
		// TODO Auto-generated method stub
		return appMapper.delPic(id);
	}
	@Override
	public String getAPKName(int id) {
		// TODO Auto-generated method stub
		return appMapper.getAPKName(id);
	}
	@Override
	public boolean UpdateStatus(AppInfo appInfoObj) {
		// TODO Auto-generated method stub
		Integer operator = appInfoObj.getModifyBy();
		AppInfo appInfo = appMapper.getAppInfo(appInfoObj.getId());
		if(null == appInfo){
			return false;
		}else{
			switch (appInfo.getStatus()) {
				case 2: //当状态为审核通过时，可以进行上架操作
					onSale(appInfo,operator,4,2);
					break;
				case 5://当状态为下架时，可以进行上架操作
					onSale(appInfo,operator,4,2);
					break;
				case 4://当状态为上架时，可以进行下架操作
					offSale(appInfo,operator,5);
					break;

			default:
				return false;
			}
		}
		return true;
	}
	private void onSale(AppInfo appInfo,Integer operator,Integer appInfStatus,Integer versionStatus) {
		offSale(appInfo,operator,appInfStatus);
		setSaleSwitchToAppVersion(appInfo,operator,versionStatus);
	}
	private boolean offSale(AppInfo appInfo,Integer operator,Integer appInfStatus) {
		AppInfo _appInfo = new AppInfo();
		_appInfo.setId(appInfo.getId());
		_appInfo.setStatus(appInfStatus);
		_appInfo.setModifyBy(operator);
		_appInfo.setOffSaleDate(new Date(System.currentTimeMillis()));
		appMapper.modify(_appInfo);
		return true;
	}
	private boolean setSaleSwitchToAppVersion(AppInfo appInfo,Integer operator,Integer saleStatus){
		AppVersion appVersion = new AppVersion();
		appVersion.setId(appInfo.getVersionId());
		appVersion.setPublishStatus(saleStatus);
		appVersion.setModifyBy(operator);
		appVersion.setModifyDate(new Date(System.currentTimeMillis()));
		appVersionMapper.modify(appVersion);
		return false;
	}
	@Override
	public boolean deleteAppInfoById(Integer delId) {
		// TODO Auto-generated method stub
		boolean flag = false;
		int versionCount = appVersionMapper.getVersionCountByAppId(delId);
		List<AppVersion> appVersionList = null;
		if(versionCount > 0){
			appVersionList = appVersionMapper.getAppVersionList(delId);
			for(AppVersion appVersion:appVersionList){
				if(appVersion.getApkLocPath() != null && !appVersion.getApkLocPath().equals("")){
					File file = new File(appVersion.getApkLocPath());
				}
			}			
			appVersionMapper.deleteVersionByAppId(delId);
		}
		AppInfo appInfo = appMapper.getAppInfo(delId);
		if(appInfo.getLogoLocPath() != null && !appInfo.getLogoLocPath().equals("")){
			File file = new File(appInfo.getLogoLocPath());
		}
		if(appMapper.deleteAppInfoById(delId) > 0){
			flag = true;
		}
		return flag;
	}
}
