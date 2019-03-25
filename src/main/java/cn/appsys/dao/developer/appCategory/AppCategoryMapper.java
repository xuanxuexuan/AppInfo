package cn.appsys.dao.developer.appCategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryMapper {
	List<AppCategory> getAppCategoryListByParentId(@Param("parentId")Integer parentId);
}
