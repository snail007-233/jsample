package com.dubbo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gome.memberCore.lang.model.UserResult;
import com.gome.memberCore.utils.utils.StringUtil;
import com.gome.userCenter.facade.login.IUserLoginFacade;
import com.gome.userCenter.model.RequestParams;
import com.gome.userCenter.model.UserInfo;
import com.gome.userCenter.model.UserLoginResult;

@Controller
public class LoginController extends BaseController{
	@Autowired	
	private IUserLoginFacade userLoginFacade; // 登录服务
	
	/**
	 * 校验用户名是否可以登录
	 * @param username
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/checkUserName")
	public Map<String, Object> checkUserName(String username, String platform){
		String invokeForm = checkForm(platform);
		if(StringUtil.isEmpty(invokeForm)){
			return ajaxReturn(100001, "参数错误");
		}
		UserResult<Map<String, Object>> result = userLoginFacade.isConflictOrSnsOrNikeName(username, invokeForm);
		System.out.println(result);
		if(result.isSuccess()){
			Map<String, Object> resultMap = result.getBuessObj();
			boolean isNickName = (Boolean)resultMap.get("isNickname"); // 是否是昵称
			String whereFrom = (String)resultMap.get("whereFrom");//用户来源
			if((!"gome".equalsIgnoreCase(whereFrom)) && (!"hqm".equalsIgnoreCase(whereFrom))){
				return ajaxReturn(200002, "第三方用户名不允许登录");
				
			}else if("hqm".equalsIgnoreCase(whereFrom) && username.startsWith("hqm-")){
				//如果是门店用户 ，不能使用hqm-开头的login去登录，不允许登录
				return ajaxReturn(200003, "门店用户 ，不能使用hqm-开头的login去登录，不允许登录");
			}else if(isNickName){
				//昵称不允许登录
				return ajaxReturn(200004, "昵称不允许登录");
			}else{
				return ajaxReturn(200, "允许登录");
			}

		}else{
			return ajaxReturn(200005, "校验用户名失败，不允许登录");
		}
	}
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @param platform
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/login")
	public Map<String, Object> login(String username, String password, String platform){
		String invokeForm = checkForm(platform);
		if(StringUtil.isEmpty(invokeForm)){
			return ajaxReturn(100001, "参数错误");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loginSite", this.site);//统一登录系统会为每个公司分配各自的站点--必填
		map.put("companyName", this.companyName);//统一登录系统会为每个公司分配自己的公司名称--必填
		map.put("isAuthorized", true);//是否是授权登录，是传true，每个公司只能授权自己的用户。授权了以后该公司的用户将能在其他渠道登录。

		RequestParams requestParams = new RequestParams();
		requestParams.setInvokeChannel(invokeForm);//调用渠道，统一登录系统分配invokeChannel--必填
		requestParams.setClientIp("127.0.0.1");//服务调用方ip—为用户ip，必填
		System.out.println(requestParams);
		//第一个参数：用户名
		//第二个参数：明文密码
		UserLoginResult<UserInfo> result = userLoginFacade.doLogin(username, password, requestParams, map);
			
		if(result.isSuccess()){
			return ajaxReturn(200, "登录成功", result.getExtraInfoMap().get("SCN"));
			
		}else{//登录失败
			return ajaxReturn(200006, result.getMessage());
		}
	}
}
