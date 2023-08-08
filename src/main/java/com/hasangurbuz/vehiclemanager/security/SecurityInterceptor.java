package com.hasangurbuz.vehiclemanager.security;

import com.google.gson.Gson;
import com.hasangurbuz.vehiclemanager.dto.User;
import com.hasangurbuz.vehiclemanager.dto.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    private RequestContext requestContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String userHeader = request.getHeader("X-User");

        if (userHeader == null){
            return false;
        }

        requestContext.setUser(new Gson().fromJson(userHeader, User.class));

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        requestContext.setUser(null);
    }
}
