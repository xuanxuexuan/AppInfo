package cn.appsys.service.developer;

import java.util.List;


import cn.appsys.pojo.AppVersion;

public interface AppVersionService {
	List<AppVersion> getAppVersionList(int id);
	boolean appsysadd(AppVersion appVersion);
	AppVersion getAppVersion(int id,int appId);
	boolean modify(AppVersion appVersion);
	int delapk(int id);
}
