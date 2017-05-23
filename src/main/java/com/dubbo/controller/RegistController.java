package com.dubbo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gome.memberCore.lang.MapResult;
import com.gome.memberCore.utils.utils.StringUtil;
import com.gome.userCenter.facade.register.IUserRegisterFacade;
import com.gome.userCenter.model.GomeUnifyRegisterUser;
import com.gome.userCenter.model.UserRegisterResult;

@Controller
public class RegistController extends BaseController{
	@Autowired	
	private IUserRegisterFacade userRegisterFacade; // 注册服务
	
	/**
	 * 校验用户是否存在
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/existsUser")
	public Map<String, Object> existsUser(String username, String platform){
		
		String invokeForm = checkForm(platform);
		if(StringUtil.isEmpty(invokeForm)){
			return ajaxReturn(100001, "参数错误");
		}
		Map<String,Object> extMap=new HashMap<String,Object>();
		extMap.put("companyName", this.companyName);//统一登录系统会为每个公司分配自己
		MapResult<Void> result = userRegisterFacade.existUser(username, invokeForm, extMap);
		if(result.isSuccess()){	
			return ajaxReturn(300002, "用户名存在");
		}else{
			return ajaxReturn(200, "用户名不存在");
		}
	}
	
	/**
	 * 注册用户
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public Map<String, Object> register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		

		String platform = request.getParameter("platform");
		String invokeForm = checkForm(platform);
		if(StringUtil.isEmpty(invokeForm)){
			return ajaxReturn(100001, "参数错误");
		}
		GomeUnifyRegisterUser gomeUser = new GomeUnifyRegisterUser();
		gomeUser.setRegisterIp(request.getParameter("ip"));//用户ip--必填
		gomeUser.setHostName(request.getServerName());//接口调用端服务器名称--必填
		gomeUser.setHostPort(request.getServerPort());//接口调用应用端口号
		gomeUser.setLogin(request.getParameter("username"));//登录名—非必填
		
		gomeUser.setPassword(request.getParameter("password"));//明文密码--必填
		gomeUser.setMobile(request.getParameter("mobile"));//手机号
		gomeUser.setRegisterType("mobileReg");//手机注册
		gomeUser.setRegisterSource(invokeForm);//统一登录系统会为每个公司分配自己的RegisterSource--必填
		gomeUser.setRegisterSite(this.site);//统一登录系统会为每个公司分配各自的站点--必填
		System.out.println(request.getParameter("mobile"));
		Map<String, Object>extMap = new HashMap<String, Object>();
		extMap.put("userRegisterType", "Person");//个人注册--必填
		extMap.put("invokeChannel", invokeForm);//统一登录系统会为每个公司分配调用渠道--必填
		extMap.put("isAuthorization", "Y");//授权码 Y是授权  N 不授权
		extMap.put("companyName", this.companyName);//统一登录系统会为每个公司分配名字--必填

		UserRegisterResult<GomeUnifyRegisterUser> result = userRegisterFacade.registerUnifyUser(gomeUser, extMap);
		if(result.isSuccess()){//result一定会返回，不用判断null，注册成功			
			return ajaxReturn(200, "注册成功");
		}else{//注册失败
			return ajaxReturn(300003, result.getMessage());
		}
	}
	
}
