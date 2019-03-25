package cn.appsys.service.developer;

import java.util.List;

import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryService {
	List<DataDictionary> getDataDictionaryList(String typeCode);

}
