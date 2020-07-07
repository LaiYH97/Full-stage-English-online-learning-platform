package com.online.college.ocSpringBootWechat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.college.domain.AuthUser;
import com.online.college.service.IAuthUserService;
import com.online.college.storage.QiniuStorage;
import com.online.college.util.EncryptUtil;
import com.online.college.web.SessionContext;
import com.online.college.wechat.process.WxMemoryCacheClient;


/**
 * 用户登录
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private IAuthUserService authUserService;
	
	/**
	 * 登录页面
	 */
	@RequestMapping(value = "/login")
	public String login(Map<String,Object> model , HttpServletRequest request){
		/*if(SessionContext.isWxLogin()){
			return "redirect:/index.html";
		}*/
		
		return "login";
	}
	
	//实现登录
	@RequestMapping(value = "/doLogin")
	public String doLogin(AuthUser user, String toUrl, Map<String,Object> model , HttpServletRequest request){
		//如果已经登录过
		/*if(SessionContext.getWxAuthUser(request) != null){
			return "redirect:/index.html";
		}*/
		
		//判断用户名密码是否正确
		AuthUser tmpAuthUser = new AuthUser();
		tmpAuthUser.setUsername(user.getUsername());
		tmpAuthUser.setPassword(EncryptUtil.encodedByMD5(user.getPassword()));
		tmpAuthUser = authUserService.getByUsernameAndPassword(tmpAuthUser);
		if(null != tmpAuthUser){
			//登录成功
			//获取当前用户openid
			String openid = WxMemoryCacheClient.getOpenid(request.getSession().getId());
			
			if(StringUtils.isNotEmpty(openid)){
				tmpAuthUser.setOpenId(openid);//设置openid，并更新数据库
				authUserService.updateSelectivity(tmpAuthUser);
			}else{
				//跳转到一个必须关注提示的页面
			}
			
			//模拟session存储数据
			String sessionId = request.getSession().getId();
			tmpAuthUser.setHeader(QiniuStorage.getUrl(tmpAuthUser.getHeader()));
			SessionContext.setAttribute(request, sessionId, tmpAuthUser);
			return "redirect:/user/index.html";
		}
		
		//登录失败
		model.put("errcode", 2);
		return "login";
	}
	
}
