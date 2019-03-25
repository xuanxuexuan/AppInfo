package cn.appsys.dao.backend.backendUser;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.BackendUser;

public interface BackendUserMapper {
	BackendUser getLoginUser(@Param("userCode")String userCode);
}
