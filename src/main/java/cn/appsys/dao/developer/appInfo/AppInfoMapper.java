package cn.appsys.dao.developer.appInfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoMapper {
	 List<AppInfo> getAppInfoList(@Param(value="softwareName")String querySoftwareName,
			@Param(value="status")Integer queryStatus,
			@Param(value="categoryLevel1")Integer queryCategoryLevel1,
			@Param(value="categoryLevel2")Integer queryCategoryLevel2,
			@Param(value="categoryLevel3")Integer queryCategoryLevel3,
			@Param(value="flatformId")Integer queryFlatformId,
			@Param(value="from")Integer currentPageNo,
			@Param(value="pageSize")Integer pageSize);
	 int getAppInfoCount(@Param(value="softwareName")String querySoftwareName,
			   @Param(value="status")Integer queryStatus,
			   @Param(value="categoryLevel1")Integer queryCategoryLevel1,
			   @Param(value="categoryLevel2")Integer queryCategoryLevel2,
			   @Param(value="categoryLevel3")Integer queryCategoryLevel3,
			   @Param(value="flatformId")Integer queryFlatformId);
	 int addAppInfo(AppInfo appInfo);
	 AppInfo getAppInfo(@Param(value="id")int id);
	 AppInfo checkName(@Param(value="APKName")String APKName);
	 int modify(AppInfo appInfo);
	 int delPic(int id);
	 String getAPKName(int id);
	 int updateVersionId(@Param(value="versionId")Integer versionId,@Param(value="id")Integer appId);
	 int updateSatus(@Param(value="status")Integer status,@Param(value="id")Integer id);
	 int deleteAppInfoById(@Param(value="id")Integer delId);
}
 