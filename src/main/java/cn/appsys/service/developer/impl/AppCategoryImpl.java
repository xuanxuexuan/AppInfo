package cn.appsys.service.developer.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.developer.appCategory.AppCategoryMapper;
import cn.appsys.pojo.AppCategory;
import cn.appsys.service.developer.AppCategoryService;

@Service
public class AppCategoryImpl implements AppCategoryService{
	@Resource
	private AppCategoryMapper appCategoryMapper;
	@Override
	public List<AppCategory> getAppCategoryListByParentId(Integer parentId) {
		// TODO Auto-generated method stub
		return appCategoryMapper.getAppCategoryListByParentId(parentId);
	}

}
