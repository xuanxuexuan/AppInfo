package cn.appsys.service.developer.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.developer.dataDictionary.DataDictionaryMapper;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.developer.DataDictionaryService;

@Service
public class DataDictionaryImpl implements DataDictionaryService{
	@Resource
	private DataDictionaryMapper dictionaryMapper;
	@Override
	public List<DataDictionary> getDataDictionaryList(String typeCode) {
		// TODO Auto-generated method stub
		return dictionaryMapper.getDataDictionaryList(typeCode);
	}
	
}
