package edu.hehai.shuili.weather.controller;

import edu.hehai.shuili.weather.api.ApiStatus;
import edu.hehai.shuili.weather.api.ReturnPureNotifyApi;
import edu.hehai.shuili.weather.api.ReturnWithResultApi;
import edu.hehai.shuili.weather.exception.ExistedException;
import edu.hehai.shuili.weather.exception.NoPermissionException;
import edu.hehai.shuili.weather.exception.NotExistedException;
import edu.hehai.shuili.weather.exception.ValidationException;
import edu.hehai.shuili.weather.intercepter.Permission;
import edu.hehai.shuili.weather.pojo.AppToken;
import edu.hehai.shuili.weather.pojo.Privilege;
import edu.hehai.shuili.weather.pojo.TokenUser;
import edu.hehai.shuili.weather.pojo.User;
import edu.hehai.shuili.weather.pojo.group.UserAdd;
import edu.hehai.shuili.weather.pojo.group.UserPassword;
import edu.hehai.shuili.weather.pojo.group.UserUpdate;
import edu.hehai.shuili.weather.service.UserService;
import edu.hehai.shuili.weather.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by yangyue
 *
 * @Date: 28/10/2017
 * @Time: 12:49 AM
 * @package_name: edu.hehai.shuili.weather.controller
 * @Description: 用户管理
 */
@RestController
@RequestMapping(value = MappingURL.USER_BASE_URL)
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(UserController.class);


    /**
     * 判断用户是否存在，是否可以注册
     * @param username
     * @return
     */
    @Permission(value = {Privilege.ADMIN, Privilege.USER})
    @RequestMapping(value = MappingURL.USER_IS_EXIST_URL, method = RequestMethod.GET)
    public ReturnPureNotifyApi isExist(
            @RequestParam(value = "username") String username
    ){
        if (userService.isExist(username))
            return new ReturnPureNotifyApi(ApiStatus.USER_EXIST);
        else
            return new ReturnPureNotifyApi(ApiStatus.USER_NOT_EXIST);
    }
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @Permission(value = {Privilege.ADMIN, Privilege.USER})
    @RequestMapping(value = MappingURL.USER_LOGIN_URL, method = RequestMethod.POST)
    public ReturnWithResultApi<String> login(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            HttpSession session
            ){
        System.out.println("attemp login: username:" + username + ", password:" + password);
        ReturnWithResultApi<String> returnWithResultApi = new ReturnWithResultApi<String>();
        User user = userService.find(username,password);
        if (user == null){
            returnWithResultApi.setApiStatus(ApiStatus.LOGIN_ERROR);
        }else {
            System.out.println("Success login: username:" + username + ", password:" + password);
            session.setAttribute("user", user);
            returnWithResultApi.setApiStatus(ApiStatus.SUCCESS);
            returnWithResultApi.setResult("login success");
        }
        return returnWithResultApi;
    }

    /**
     * 删除用户操作[管理员]
     * @param userId
     * @return
     */
    @Permission(value = {Privilege.ADMIN})
    @RequestMapping(value = MappingURL.USER_DELETE_URL, method = RequestMethod.DELETE)
    public ReturnPureNotifyApi delete(
            @PathVariable int userId
    ){
        if (userService.find(userId) == null){
            throw new NotExistedException("userId", userId);
        }
        userService.delete(userId);
        return new ReturnPureNotifyApi(ApiStatus.SUCCESS);
    }


    /**
     * 用户注册接口
     * @param user
     * @return
     */
    @Permission(value = {Privilege.ADMIN, Privilege.USER})
    @RequestMapping(value = MappingURL.USER_REGISTER_URL, method = RequestMethod.POST)
    public ReturnPureNotifyApi register(
            @Validated( { UserAdd.class }) @RequestBody User user,
            BindingResult validResult
    ){
        System.out.println(user);
        if (validResult.hasErrors()){
            throw new ValidationException(validResult.getFieldErrors());
        }else if (userService.isExist(user.getUsername())){
            throw new ExistedException("username", user.getUsername());
        }
        userService.add(user);
        return new ReturnPureNotifyApi(ApiStatus.SUCCESS);
    }

    /**
     * 更新用户基本属性 [如果是管理员，可以更改用户权限]
     * @param user
     * @param request
     * @param validResult
     * @return
     */
    @Permission(value = {Privilege.USER, Privilege.ADMIN})
    @RequestMapping(value = MappingURL.USER_PROFILE_UPDATE, method = RequestMethod.PUT)
    public ReturnPureNotifyApi update(
            @Validated({UserUpdate.class}) User user,
            BindingResult validResult,
            HttpServletRequest request
            ){
        if (validResult.hasErrors()){
            throw new ValidationException(validResult.getFieldErrors());
        }else if (userService.find(user.getUserId()) == null){
            throw new NotExistedException("userId", user.getUserId());
        }
        TokenUser tokenUser = JavaWebTokenUtil.unsign(request.getHeader("appSecret"), TokenUser.class);
        assert tokenUser != null;
        if (userService.find(tokenUser.getUserId()).getPrivilege() == Privilege.ADMIN.getCode()){
            //如果操作员是管理员，可以更改自己以及其他用户的信息,包括权限
            userService.superUpdate(user);
        }else {
            //如果操作员是普通用户，当用户id和要更改的用户id不一致，返回没有权限异常
            if(tokenUser.getUserId() != user.getUserId()){
                throw new NoPermissionException();
            }
            userService.update(user);
        }
        return new ReturnPureNotifyApi(ApiStatus.SUCCESS);
    }


    /**
     * 更新密码
     * @param user
     * @param validResult
     * @return
     */
    @Permission(value = {Privilege.USER, Privilege.ADMIN})
    @RequestMapping(value = MappingURL.USER_PASSWORD_UPDATE, method = RequestMethod.PUT)
    public ReturnPureNotifyApi updatePassword(
            @Validated({UserPassword.class}) User user,
            BindingResult validResult,
            HttpServletRequest request
            ){
        if (validResult.hasErrors()){
            throw new ValidationException(validResult.getFieldErrors());
        }else if (userService.find(user.getUserId()) == null){
            throw new NotExistedException("userId", user.getUserId());
        }
        TokenUser tokenUser = JavaWebTokenUtil.unsign(request.getHeader("appSecret"), TokenUser.class);
        assert tokenUser != null;
        //是管理员，为所欲为
        if (userService.find(tokenUser.getUserId()).getPrivilege() == Privilege.ADMIN.getCode()){
            userService.updatePassword(user);
            return new ReturnPureNotifyApi(ApiStatus.SUCCESS);
        }else if((tokenUser.getUserId() != user.getUserId())) {//不是管理员但是想更改别人的抛出异常
            throw new NoPermissionException();
        }
        userService.updatePassword(user);
        return new ReturnPureNotifyApi(ApiStatus.SUCCESS);
    }

}
