package cn.appsys.dao.developer.dataDictionary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryMapper {
	List<DataDictionary> getDataDictionaryList(@Param("typeCode")String typeCode);
}
