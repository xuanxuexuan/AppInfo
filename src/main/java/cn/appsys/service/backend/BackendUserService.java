package cn.appsys.service.backend;


import cn.appsys.pojo.BackendUser;

public interface BackendUserService {
	BackendUser getLoginUser(String userCode,String userPassword);
}
