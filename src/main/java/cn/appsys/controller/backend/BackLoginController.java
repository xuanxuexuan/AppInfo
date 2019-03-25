package cn.appsys.controller.backend;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.BackendUser;
import cn.appsys.service.backend.BackendUserService;
import cn.appsys.tools.Constants;


@Controller
@RequestMapping(value="/back")
public class BackLoginController {
	
	@Resource
	private BackendUserService backService;
	
	@RequestMapping(value="/login")
	public String login(){
		return "backendlogin";
	}
	@RequestMapping(value="/dologin")
	public String dologin(@RequestParam String userCode,@RequestParam String userPassword,HttpServletRequest request,HttpSession session){
		BackendUser user=backService.getLoginUser(userCode, userPassword);
		if(null != user){
			//放入session
			session.setAttribute(Constants.USER_SESSION, user);
			return "redirect:/back/flatform/main";
		}else{
			//页面跳转(错误）
			request.setAttribute("error", "用户名或密码不正确");
			return "backendlogin";
		}
		
	}
	@RequestMapping(value="/flatform/main")
	public String main(HttpSession session){
		if(session.getAttribute(Constants.USER_SESSION) == null){
			return "redirect:/back/login";
		}
		return "backend/main";
	}
	@RequestMapping(value="/logout")
	public String logout(HttpSession session){
		//清除session
		session.invalidate();
		return "backendlogin";
	}
}
