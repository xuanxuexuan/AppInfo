package cn.appsys.service.developer.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.developer.devuser.DevUserMapper;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.developer.DevUserService;

@Service
public class DevUserServiceImpl implements DevUserService{
	@Resource
	private DevUserMapper devmapper;
	@Override
	public DevUser login(String devCode, String devPassword) {
		// TODO Auto-generated method stub
		DevUser user = null;
		user = devmapper.getLoginUser(devCode);
		if(null != user){
			if(!user.getDevPassword().equals(devPassword))
				user = null;
		}
		return user;
	}
}
