package com.dubbo.controller;

import java.util.HashMap;
import java.util.Map;

public class BaseController {
		
	protected Map<String, String> invokeFrom;
	
	/* 定义调用dubbo参数 */
	protected String companyName = "gomeOnLine"; // 调用公司
	protected String site = "video"; // 站点
	
	/* 定义错误码*/
	
	/**
	 * 初始化
	 */
	public BaseController(){
		this.invokeFrom = new HashMap<String, String>();
		this.invokeFrom.put("app", "videoMobile");
		this.invokeFrom.put("wap", "videoWap");
		this.invokeFrom.put("web", "videoWeb");
	}
	
	/**
	 * 获取invokeForm
	 * @param platform
	 * @return
	 */
	public String checkForm(String platform){
		if(this.invokeFrom.containsKey(platform)){
			return this.invokeFrom.get(platform);
		}
		return "";
	}
	
	public Map<String, Object> ajaxReturn(Integer code, String message){
		Map<String, Object> temp=new HashMap<String, Object>();
		temp.put("code", code);
		temp.put("message", message);
		return temp;
	}
	
	public Map<String, Object> ajaxReturn(Integer code, String message, Object data){
		Map<String, Object> temp=new HashMap<String, Object>();
		temp.put("code", code);
		temp.put("message", message);
		temp.put("data", data);
		return temp;
	}
}
