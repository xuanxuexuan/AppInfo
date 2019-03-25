package cn.appsys.service.developer.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.developer.appInfo.AppInfoMapper;
import cn.appsys.dao.developer.appVersion.AppVersionMapper;
import cn.appsys.pojo.AppVersion;
import cn.appsys.service.developer.AppVersionService;
@Service
public class AppVersionImpl implements AppVersionService{
	@Resource
	private AppVersionMapper appVersionMapper;
	@Resource
	private AppInfoMapper appInfoMapper;
	@Override
	public List<AppVersion> getAppVersionList(int id) {
		// TODO Auto-generated method stub
		return appVersionMapper.getAppVersionList(id);
	}
	@Override
	public boolean appsysadd(AppVersion appVersion) {
		// TODO Auto-generated method stub
		boolean flag = false;
		Integer versionId = null;
		if(appVersionMapper.add(appVersion) > 0){
			versionId = appVersion.getId();
			flag = true;
		}
		if(appInfoMapper.updateVersionId(versionId, appVersion.getAppId()) > 0 && flag){
			flag = true;
		}
		return flag;
	}
	@Override
	public AppVersion getAppVersion(int id, int appId) {
		// TODO Auto-generated method stub
		return appVersionMapper.getAppVersion(id, appId);
	}
	@Override
	public boolean modify(AppVersion appVersion) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(appVersionMapper.modify(appVersion) > 0){
			flag = true;
		}
		return flag;
	}
	@Override
	public int delapk(int id) {
		// TODO Auto-generated method stub
		return appVersionMapper.delapk(id);
	}
	

}
