package com.transam.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.transam.system.entity.Resources;
import com.transam.system.entity.User;
import com.transam.system.service.ResourcesService;
import com.transam.system.service.UserService;
import com.transam.utils.Common;
import com.transam.utils.PasswordHelper;
import com.transam.utils.Result;
import com.transam.utils.TreeUtil;


@Controller
public class LoginController {
	@Autowired
	private ResourcesService resourceService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login(HttpServletRequest request){
		request.removeAttribute("error");
		return "/login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String username, String password, HttpServletRequest request) {
		try {
			if (!request.getMethod().equals("POST")) {
				request.setAttribute("error", "支持POST方法提交！");
				return "/login";
			}
			if (StringUtils.isBlank(username)||StringUtils.isBlank(password)){
				request.setAttribute("error", "用户名或密码不能为空！");
				return "/login";
			}
			Subject user = SecurityUtils.getSubject();
			// 用户输入的账号和密码,,存到UsernamePasswordToken对象中..然后由shiro内部认证对比,
			// 认证执行者交由ShiroDbRealm中doGetAuthenticationInfo处理
			// 当以上认证成功后会向下执行,认证失败会抛出异常
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			try {
				user.login(token);
			} catch (LockedAccountException lae) {
				token.clear();
				request.setAttribute("error", "用户已经被锁定不能登录，请与管理员联系！");
				return "/login";
			} catch(AccountException e) {
				token.clear();
				request.setAttribute("error", "您没有登录权限，请与管理员联系！");
				return "/login";
			} catch (AuthenticationException e) {
				token.clear();
				request.setAttribute("error", "用户或密码不正确！");
				return "/login";
			}
			
			request.removeAttribute("error");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "登录异常，请联系管理员！");
			return "/login";
		}
		return "redirect:index";
	}
	
	@RequestMapping("/index")
	public String index(Model model) throws Exception {
		String roleIds = Common.findSessionId("roleSessionId");
		List<Resources> list = resourceService.findResByRoleIds(roleIds);
		TreeUtil treeUtil = new TreeUtil();
		List<Resources> ns = treeUtil.getChildResources(list, 0);
		model.addAttribute("tabsList", ns);
		return "/index";
	}
	
	@RequestMapping(value="/menu",method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> getMenu() throws Exception {
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		String roleIds = Common.findSessionId("roleSessionId");
		List<Resources> list = resourceService.findResByRoleIds(roleIds);
		TreeUtil treeUtil = new TreeUtil();
		List<Resources> ns = treeUtil.getChildResources(list, 0);
		jsonMap.put("list", ns);
		return jsonMap;
	}
	
	/**
	 * 跳转到忘记密码页面
	 * @return
	 */
	@RequestMapping(value="/editPwd",method=RequestMethod.GET)
	public String forgetPwd(){
		return "/editPwd";
	}
	
	@RequestMapping(value="/editPwd",method=RequestMethod.POST)
	public @ResponseBody Result forgetPwd(Model model,String rePassword,String newPassword,String oldPassword){
		if(StringUtils.isBlank(rePassword) || StringUtils.isBlank(newPassword) || StringUtils.isBlank(oldPassword)){
			return Result.build(500, "输入的内容不能为空！");
		}
		if(!newPassword.equals(rePassword)){
			return Result.build(500, "两次输入密码不一致！");
		}
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userSession");
		PasswordHelper passwordHelper = new PasswordHelper();
		String encryptPassword = passwordHelper.encryptPassword(user.getAccountName(), oldPassword, user.getCredentialsSalt());
		if(!user.getPassword().equalsIgnoreCase(encryptPassword)){
			return Result.build(500, "原密码不正确!");
		}
		user.setPassword(newPassword);
		passwordHelper.encryptPassword(user);
		userService.update(user);
		return Result.ok();
	}
	
	/**
	 * 注销
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout() {
		// 使用权限管理工具进行用户的退出，注销登录
		SecurityUtils.getSubject().logout(); // session
											// 会销毁，在SessionListener监听session销毁，清理权限缓存
		return "redirect:login.shtml";
	}
}
