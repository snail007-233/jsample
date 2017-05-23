package com.dubbo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gome.memberCore.lang.model.UserResult;
import com.gome.memberCore.utils.utils.StringUtil;
import com.gome.sso.common.outside.SsoUserCookieToolsOutside;
import com.gome.userCenter.facade.userservice.profile.IEmailMobileFacade;
import com.gome.userCenter.facade.userservice.profile.IUserInfoFacade;
import com.gome.userCenter.model.UserInfo;

@Controller
public class UserController extends BaseController{
		
	@Autowired
	private IUserInfoFacade userInfoFacade; // 用户信息
	
	@Autowired
	private IEmailMobileFacade mobileFacade;
	
	@Autowired
	private SsoUserCookieToolsOutside ssoUserCookieToolsOutside;
	
	/**
	 * 获取用户信息
	 * @param userId
	 * @param platform
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getUserInfoById")
	public Map<String, Object> getUserInfoById(String userId, String platform){
		String invokeForm = checkForm(platform);
		if(StringUtil.isEmpty(invokeForm)){
			return ajaxReturn(100001, "参数错误");
		}
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyName", this.companyName);
		UserResult<UserInfo> result = userInfoFacade.getItemById(userId, invokeForm);
		if(result.isSuccess()){
			
			return ajaxReturn(200, result.getMessage(), result.getBuessObj());
			
		}else{
			return ajaxReturn(100002, result.getMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping("/checkMobile")
	public Map<String, Object> checkMobile(String mobile, String platform){
		String invokeForm = checkForm(platform);
		if(StringUtil.isEmpty(invokeForm)){
			return ajaxReturn(100001, "参数错误");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("companyName", this.companyName);
		UserResult<Boolean> result = mobileFacade.isEmailOrMobileUsed(mobile, invokeForm, params);
		if(result.isSuccess()){
			return ajaxReturn(200, "可以使用");
		}else{
			return ajaxReturn(100002, result.getMessage());
		}
	}
}
