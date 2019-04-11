package edu.hehai.shuili.weather.intercepter;

import edu.hehai.shuili.weather.exception.NoPermissionException;
import edu.hehai.shuili.weather.pojo.Privilege;
import edu.hehai.shuili.weather.pojo.TokenUser;
import edu.hehai.shuili.weather.pojo.User;
import edu.hehai.shuili.weather.util.JavaWebTokenUtil;
import edu.hehai.shuili.weather.util.MD5Util;
import edu.hehai.shuili.weather.util.MappingURL;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class PermissionsInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        User user = (User)request.getSession().getAttribute( "user");
        if(user == null){
            response.sendRedirect(MappingURL.LOGIN_PAGE);
            return false;
        }else if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            return roleControl(request, response, handlerMethod, user);
        }
        response.sendRedirect(MappingURL.LOGIN_PAGE);
        return false;
    }


    private boolean roleControl(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, User user){
        Object target=handlerMethod.getBean();
        Class<?> clazz=handlerMethod.getBeanType();
        Method m=handlerMethod.getMethod();
        if (clazz!=null && m!=null){
            boolean isClzAnnotation= clazz.isAnnotationPresent(Permission.class);
            boolean isMethondAnnotation=m.isAnnotationPresent(Permission.class);
            Permission rc=null;
            if(isMethondAnnotation){
                rc=m.getAnnotation(Permission.class);
            }else if(isClzAnnotation){
                rc=clazz.getAnnotation(Permission.class);
            }
            int privilege = user.getPrivilege();
            assert rc != null;
            for (Privilege i:rc.value()){//如果在权限表中找到
                if (i.getCode() == privilege){
                    return true;
                }
            }
        }
        return false;
    }

}