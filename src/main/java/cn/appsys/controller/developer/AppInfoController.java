package cn.appsys.controller.developer;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.developer.AppCategoryService;
import cn.appsys.service.developer.AppInfoService;
import cn.appsys.service.developer.AppVersionService;
import cn.appsys.service.developer.DataDictionaryService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping(value="/dev/flatform/app")
public class AppInfoController {
	@Resource
	private AppVersionService appVersionService;
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private DataDictionaryService dictionaryService;
	@Resource
	private AppCategoryService appCategoryService;
	@RequestMapping(value="/list")
	public String getApplist(Model model,HttpSession session,
			@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
			@RequestParam(value="queryStatus",required=false) String _queryStatus,
			@RequestParam(value="queryCategoryLevel1",required=false) String _queryCategoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required=false) String _queryCategoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required=false) String _queryCategoryLevel3,
			@RequestParam(value="queryFlatformId",required=false) String _queryFlatformId,
			@RequestParam(value="pageIndex",required=false) String pageIndex){
		//获取App信息
		List<AppInfo> appInfoList=null;
		//所属平台
		List<DataDictionary> flatList=null;
		//审核状态
		List<DataDictionary> statusList=null;
		//一二三级分类
		List<AppCategory> categoryLevel1List = null;
		List<AppCategory> categoryLevel2List = null;
		List<AppCategory> categoryLevel3List = null;
		//页面容量
		int pageSize = Constants.pageSize;
		//当前页码
		int currentPageNo = 1;
		if(pageIndex != null){
			currentPageNo = Integer.valueOf(pageIndex);
		}
		Integer queryStatus = null;
		if(_queryStatus != null && !_queryStatus.equals("")){
			queryStatus = Integer.parseInt(_queryStatus);
		}
		Integer queryCategoryLevel1 = null;
		if(_queryCategoryLevel1 != null && !_queryCategoryLevel1.equals("")){
			queryCategoryLevel1 = Integer.parseInt(_queryCategoryLevel1);
		}
		Integer queryCategoryLevel2 = null;
		if(_queryCategoryLevel2 != null && !_queryCategoryLevel2.equals("")){
			queryCategoryLevel2 = Integer.parseInt(_queryCategoryLevel2);
		}
		Integer queryCategoryLevel3 = null;
		if(_queryCategoryLevel3 != null && !_queryCategoryLevel3.equals("")){
			queryCategoryLevel3 = Integer.parseInt(_queryCategoryLevel3);
		}
		Integer queryFlatformId = null;
		if(_queryFlatformId != null && !_queryFlatformId.equals("")){
			queryFlatformId = Integer.parseInt(_queryFlatformId);
		}
		//获取总数量
		int totalCount = appInfoService.getAppInfoCount(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId);
		//总页数
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		//控制首页和尾页
		if(currentPageNo < 1){
			currentPageNo = 1;
		}else if(currentPageNo > totalPageCount){
			currentPageNo = totalPageCount;
		}
		
		//获取App信息
		appInfoList = appInfoService.getAppInfoList(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId,(currentPageNo-1)*5, pageSize);
		//审核状态
		statusList = dictionaryService.getDataDictionaryList("APP_STATUS");
		flatList = dictionaryService.getDataDictionaryList("APP_FLATFORM");
		categoryLevel1List = appCategoryService.getAppCategoryListByParentId(null);
		model.addAttribute("appInfoList", appInfoList);
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatList", flatList);
		session.setAttribute("flatList", flatList);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		if(queryCategoryLevel2 != null && !queryCategoryLevel2.equals("")){
			categoryLevel2List = getCategoryList(queryCategoryLevel1.toString());
			model.addAttribute("categoryLevel2List", categoryLevel2List);
		}
		if(queryCategoryLevel3 != null && !queryCategoryLevel3.equals("")){
			categoryLevel3List = getCategoryList(queryCategoryLevel2.toString());
			model.addAttribute("categoryLevel3List", categoryLevel3List);
		}
		model.addAttribute("pages", pages);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		model.addAttribute("queryFlatformId", queryFlatformId);
		return "developer/appinfolist";
	}
	@RequestMapping(value="/categorylevellist.json",method=RequestMethod.GET)
	@ResponseBody
	public List<AppCategory> getAppCategoryList (@RequestParam String pid){
		if(pid.equals("")) pid = null;
		return getCategoryList(pid);
	}
	
	public List<AppCategory> getCategoryList (String pid){
		List<AppCategory> categoryLevelList = null;
		try {
			categoryLevelList = appCategoryService.getAppCategoryListByParentId(pid==null?null:Integer.parseInt(pid));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categoryLevelList;
	}
	
	@RequestMapping(value="/appinfoadd",method=RequestMethod.GET)
	public String add(@ModelAttribute("appInfo") AppInfo appInfo){
		return "developer/appinfoadd";
	}
	@RequestMapping(value="/appinfoaddsave",method=RequestMethod.POST)
	public String addSave(AppInfo appInfo,HttpSession session,HttpServletRequest request,
					@RequestParam(value="a_logoPicPath",required= false) MultipartFile attach){	
		String logoPicPath =  null;
		String logoLocPath =  null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+java.io.File.separator+"uploadfiles");
			String oldFileName = attach.getOriginalFilename();//原文件名
			String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀名
			int filesize=1000000000;
			if(attach.getSize()>filesize){
				request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_4);
				return "developer/appinfoadd";
			}else if(prefix.equalsIgnoreCase("jpg")||prefix.equalsIgnoreCase("png")||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){
				String fileName=System.currentTimeMillis()+RandomUtils.nextInt(1000000000)+"_Personal.jpg";
				File targetFile=new File(path,fileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				try{
					attach.transferTo(targetFile);
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
					return "developer/appinfoadd";
				}
				logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				logoLocPath = path+File.separator+fileName;
			}else{
				request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
				return "developer/appinfoadd";
			}
		}
		appInfo.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setCreationDate(new Date());
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setDevId(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setStatus(1);
		if(appInfoService.addAppInfo(appInfo)){
			return "redirect:/dev/flatform/app/list";
		}
		return "developer/appinfoadd";
	}
	@RequestMapping(value="/apkexist.json",method=RequestMethod.GET)
	@ResponseBody
	public Object apkNameIsExit(@RequestParam String APKName){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(APKName.equals("")||APKName.isEmpty()){
			resultMap.put("APKName", "empty");
		}else{
			AppInfo app=appInfoService.checkName(APKName);
			if(app!=null){
				resultMap.put("APKName", "exist");
			}else{
				resultMap.put("APKName", "noexist");
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
	@RequestMapping(value="/appinfomodify")
	public String modifyAppInfo(@RequestParam("id") int id,Model model){
		AppInfo appinfo=appInfoService.getAppInfo(id);
		model.addAttribute(appinfo);
		return "developer/appinfomodify";
	}
	@RequestMapping(value="/appinfomodifysave",method=RequestMethod.POST)
	public String modifySave(AppInfo appInfo,HttpSession session,HttpServletRequest request,
							@RequestParam(value="attach",required= false) MultipartFile attach){		
		String logoPicPath =  null;
		String logoLocPath =  null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName = attach.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			int filesize = 100000000;
			if(attach.getSize() > filesize){
            	 return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()+"&error=error4";
            }else if(prefix.equalsIgnoreCase("jpg")||prefix.equalsIgnoreCase("png")||prefix.equalsIgnoreCase("jepg")||prefix.equalsIgnoreCase("pneg")){//上传图片格式
            	String fileName=System.currentTimeMillis()+RandomUtils.nextInt(1000000000)+"_Personal.jpg";
				 File targetFile = new File(path,fileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()+"&error=error2";
				} 
				 logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				 logoLocPath = path+File.separator+fileName;
            }else{
            	return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()+"&error=error3";
            }
		}
		appInfo.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setModifyDate(new Date());
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setLogoPicPath(logoPicPath);
		try {
			if(appInfoService.modify(appInfo)){
				return "redirect:/dev/flatform/app/list";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "developer/appinfomodify";
	}
	@RequestMapping(value="/delfile.json",method=RequestMethod.GET)
	@ResponseBody
	public String delfile(@RequestParam int id){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		int result=appInfoService.delPic(id);
		if(result>0){
			resultMap.put("result", "success");
		}else{
			resultMap.put("result", "failed");
		}
		return JSONArray.toJSONString(resultMap);
	}
	@RequestMapping(value="/appview/{id}")
	public String appview(@PathVariable int id,Model model){
		AppInfo appinfo=appInfoService.getAppInfo(id);
		List<AppVersion> appVersionList=appVersionService.getAppVersionList(id);
		model.addAttribute(appinfo);
		model.addAttribute("appVersionList",appVersionList);
		return "developer/appinfoview";
	}
	@RequestMapping(value="/appversionadd",method=RequestMethod.GET)
	public String addVersion(@RequestParam(value="id")int appId,String fileUploadError,AppVersion appVersion,Model model){
		appVersion.setAppId(appId);
		List<AppVersion> appVersionList=appVersionService.getAppVersionList(appId);
		String AppName=appInfoService.getAPKName(appId);
		appVersion.setAppName(AppName);
		model.addAttribute(appVersion);
		model.addAttribute("appVersionList", appVersionList);
		return "developer/appversionadd";
	}
	@RequestMapping(value="/addversionsave",method=RequestMethod.POST)
	public String addversionsave(AppVersion appVersion,HttpSession session,HttpServletRequest request,
			@RequestParam(value="a_downloadLink",required= false) MultipartFile attach ){
		String downloadLink =  null;
		String apkLocPath = null;
		String apkFileName = null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName = attach.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			if(prefix.equalsIgnoreCase("apk")){
				 apkFileName = oldFileName + "-" +appVersion.getVersionNo() + ".apk";
				 File targetFile = new File(path,apkFileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()+"&error=error2";
				} 
				downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
				apkLocPath = path+File.separator+apkFileName;
			}else{
				return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()+"&error=error3";
			}
		}
		appVersion.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setModifyDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(apkFileName);
		if(appVersionService.appsysadd(appVersion)){
			return "redirect:/dev/flatform/app/list";
		}
		return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId();
	}
	@RequestMapping(value="/appversionmodify")
	public String appversionmodify(@RequestParam("vid") int versionId,@RequestParam("aid") int appId,Model model){
		AppVersion appVersion=appVersionService.getAppVersion(versionId, appId);
		List<AppVersion> appVersionList=appVersionService.getAppVersionList(appId);
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appVersionList", appVersionList);
		return "developer/appversionmodify";
	}
	@RequestMapping(value="/appversionmodifysave",method=RequestMethod.POST)
	public String modifyAppVersionSave(AppVersion appVersion,HttpSession session,HttpServletRequest request,
					@RequestParam(value="attach",required= false) MultipartFile attach){	
		String downloadLink =  null;
		String apkLocPath = null;
		String apkFileName = null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			String oldFileName = attach.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			if(prefix.equalsIgnoreCase("apk")){
				 apkFileName = oldFileName + "-" +appVersion.getVersionNo() + ".apk";
				 File targetFile = new File(path,apkFileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()+"&error=error2";
				} 
				downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
				apkLocPath = path+File.separator+apkFileName;
			}else{
				return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()+"&error=error3";
			}
		}
		appVersion.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setModifyDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(apkFileName);
		if(appVersionService.modify(appVersion)){
			return "redirect:/dev/flatform/app/list";
		}
		return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId();
	}
	@RequestMapping(value="/delapk.json",method=RequestMethod.GET)
	@ResponseBody
	public String delapk(@RequestParam int id){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		int result=appVersionService.delapk(id);
		if(result>0){
			resultMap.put("result", "success");
		}else{
			resultMap.put("result", "failed");
		}
		return JSONArray.toJSONString(resultMap);
	}
	@RequestMapping(value="/delapp.json")
	@ResponseBody
	public Object delApp(@RequestParam String id){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(id)){
			resultMap.put("delResult", "notexist");
		}else{
			try {
				if(appInfoService.deleteAppInfoById(Integer.parseInt(id)))
					resultMap.put("delResult", "true");
				else
					resultMap.put("delResult", "false");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
	@RequestMapping(value="/{appid}/sale",method=RequestMethod.PUT)
	@ResponseBody
	public Object sale(@PathVariable String appid,HttpSession session){
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Integer appIdInteger = 0;
		try{
			appIdInteger = Integer.parseInt(appid);
		}catch(Exception e){
			appIdInteger = 0;
		}
		resultMap.put("errorCode", "0");
		resultMap.put("appId", appid);
		if(appIdInteger>0){
			try {
				DevUser devUser = (DevUser)session.getAttribute(Constants.DEV_USER_SESSION);
				AppInfo appInfo = new AppInfo();
				appInfo.setId(appIdInteger);
				appInfo.setModifyBy(devUser.getId());
				if(appInfoService.UpdateStatus(appInfo)){
					resultMap.put("resultMsg", "success");
				}else{
					resultMap.put("resultMsg", "success");
				}		
			} catch (Exception e) {
				resultMap.put("errorCode", "exception000001");
			}
		}else{
			//errorCode:0为正常
			resultMap.put("errorCode", "param000001");
		}
		return resultMap;
	}
}
