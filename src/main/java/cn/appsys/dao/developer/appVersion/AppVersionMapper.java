package cn.appsys.dao.developer.appVersion;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionMapper {
	List<AppVersion> getAppVersionList(int id);
	int add(AppVersion appVersion);
	AppVersion getAppVersion(@Param("id")int id,@Param("appId")int appId);
	int modify(AppVersion appVersion);
	int delapk(@Param("id")int id);
	int getVersionCountByAppId(@Param("appId")Integer appId);
	int deleteVersionByAppId(@Param("appId")Integer appId);
}
