package cn.appsys.service.backend.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.backend.backendUser.BackendUserMapper;
import cn.appsys.pojo.BackendUser;
import cn.appsys.service.backend.BackendUserService;

@Service
public class BackendUserServiceImpl implements BackendUserService{
	@Resource
	private BackendUserMapper backMapper;
	@Override
	public BackendUser getLoginUser(String userCode, String userPassword) {
		// TODO Auto-generated method stub
		BackendUser user = null;
		user = backMapper.getLoginUser(userCode);
		if(null != user){
			if(!user.getUserPassword().equals(userPassword))
				user = null;
		}
		return user;
	}

}
