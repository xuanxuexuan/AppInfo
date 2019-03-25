package cn.appsys.service.developer;

import java.util.List;


import cn.appsys.pojo.AppCategory;

public interface AppCategoryService {
	List<AppCategory> getAppCategoryListByParentId(Integer parentId);
}
