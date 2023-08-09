package com.hasangurbuz.vehiclemanager.security;

import com.google.gson.Gson;
import com.hasangurbuz.vehiclemanager.dto.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String userHeader = request.getHeader("X-User");

        if (userHeader == null) {
            return false;
        }

        UserContext userContextThreadLocal = UserContext.create();

        UserContext context = new Gson().fromJson(userHeader, UserContext.class);

        userContextThreadLocal.setUserId(context.getUserId());
        userContextThreadLocal.setUserRole(context.getUserRole());
        userContextThreadLocal.setName(context.getName());
        userContextThreadLocal.setSurname(context.getSurname());
        userContextThreadLocal.setCompanyId(context.getCompanyId());
        userContextThreadLocal.setCompanyName(context.getCompanyName());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserContext.unset();
    }
}
