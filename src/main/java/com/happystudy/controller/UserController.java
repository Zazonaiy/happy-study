package com.happystudy.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.system.UserInfo;
import com.happystudy.constants.Constants;
import com.happystudy.model.User;
import com.happystudy.model.User_Info;
import com.happystudy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.happystudy.util.camelToUnderline.camelToUnderline;

@Controller
@RequestMapping("/happy-study/user")
public class UserController {
    @Autowired
    UserService userService;
    
	@RequestMapping("")
	public String index() {
		return "user_man";
	}
	
	@RequestMapping("propertyMan")
	public String gotoPropMan() {
		return "property_man";
	}

    //修改密码
    @ResponseBody
    public JSONObject updateUser(String username, String oldPassword, String newPassword) {
        if (username.isEmpty()||username.trim().isEmpty()){
            return new JSONObject().set("status",Constants.NULL_USER);
        }
        else {
            return userService.updateUser(username,oldPassword,newPassword);
        }
    }

    //注册用户
    @ResponseBody
    public JSONObject registUser(String username, String password) {
        if (username.isEmpty()||username.trim().isEmpty()||password.isEmpty()||password.trim().isEmpty()){
            return new JSONObject().set("status",Constants.NULL_USER);
        }
        else {
            return this.userService.registUser(username,password);
        }
    }

    //绑定手机
    @ResponseBody
    public JSONObject bindPhone(String username, String phonenum) {
        if (username.isEmpty()||username.trim().isEmpty()){
            return new JSONObject().set("status",Constants.NULL_USER);
        }
        else
        {
            return this.userService.bindPhone(username,phonenum);
        }
    }

    //注销
    @ResponseBody
    public void loginOut(HttpServletRequest httpServletRequest)
    {
        httpServletRequest.getSession().invalidate();
    }

    //修改用户个人信息
    @ResponseBody
    public JSONObject updateUserInfo(Map<String, Object> userinfo)
    {
        if (userinfo.get("i_name")==null) return new JSONObject().set("status",Constants.NULL_USER);
        else {
            Map<String,Object> param=new HashMap<String,Object>();
            Field[] fields=UserInfo.class.getDeclaredFields();//反射获取属性名
            String[] fieldsname=new String[fields.length];
            for (int i=0;i<fields.length;i++)
            {
                fieldsname[i]=camelToUnderline(fields[i].getName());
                if (userinfo.get(fieldsname[i])!=null)
                {
                    param.put(fieldsname[i],userinfo.get(fieldsname[i]));
                }
            }
            return this.userService.updateUserInfo(param);
        }
    }

    //查询已存在用户(5个参数)
    @ResponseBody
    public JSONObject queryUser(Map<String, Object> param){
        Map<String,Object> newParam=new HashMap<String,Object>();
        if (param.get("keyword")==null){
            newParam.put("keyword","u_username");
        } else newParam.put("keyword",param.get("keyword"));

        if (param.get("orderBy")==null){
            newParam.put("orderBy","u_username");
        }else newParam.put("orderBy",param.get("orderBy"));

        if (param.get("orderWay")==null){
            newParam.put("orderWay","asc");
        }else newParam.put("orderWay",param.get("orderWay"));

        if (param.get("pageOffset")==null){
            newParam.put("pageOffset","1");
        }else newParam.put("pageOffset",param.get("pageOffset"));

        if (param.get("pageSize")==null){
            newParam.put("pageSize","5");
        }else newParam.put("pageSize",param.get("pageSize"));

        return this.userService.queryUser(newParam);
    }
    //查询用户个人信息
    @ResponseBody
    public JSONObject queryUserInfo(String username){
        if (username.trim().isEmpty() || username.isEmpty()){
            return new JSONObject().set("status",Constants.NULL_USER);
        }
        else return this.userService.queryUserInfo(username);
    }

    //查询用户角色名称
    @ResponseBody
    public JSONObject queryUserRole(String username){
        if (username.trim().isEmpty() || username.isEmpty()){
            return new JSONObject().set("status",Constants.NULL_USER);
        }
        else return this.userService.queryUserRole(username);
    }

    //删除用户
    @ResponseBody
    public JSONObject delUserByName(String username){
        if (username.trim().isEmpty() || username.isEmpty()){
            return new JSONObject().set("status",Constants.NULL_USER);
        }
        else return this.userService.delUserByName(username);
    }

}
