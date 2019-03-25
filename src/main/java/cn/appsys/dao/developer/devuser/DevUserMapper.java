package cn.appsys.dao.developer.devuser;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DevUser;

public interface DevUserMapper {
	DevUser getLoginUser(@Param("devCode")String devCode);
}
