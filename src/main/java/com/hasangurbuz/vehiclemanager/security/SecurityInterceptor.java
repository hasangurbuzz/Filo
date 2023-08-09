package com.hasangurbuz.vehiclemanager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hasangurbuz.vehiclemanager.api.ApiContext;
import org.apache.http.entity.ContentType;
import org.openapitools.model.ErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String userHeader = request.getHeader("X-User");

        if (userHeader == null || userHeader.isEmpty()) return false;
        ApiContext apiContext = objectMapper.readValue(userHeader, ApiContext.class);


        if (apiContext.getUserId() == null || apiContext.getCompanyId() == null) {
            ErrorDTO error = new ErrorDTO();
            error.setCode("E0101");
            error.setMessage("Auth error");
            response.setContentType(ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8).toString());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            try {
                response.getWriter().println(objectMapper.writeValueAsString(error));
            } catch (Exception e) {

                System.out.println(e);

            }

            return false;

        }


        ApiContext apiContextThreadLocal = ApiContext.create();


        apiContextThreadLocal.setUserId(apiContext.getUserId());
        apiContextThreadLocal.setUserRole(apiContext.getUserRole());
        apiContextThreadLocal.setName(apiContext.getName());
        apiContextThreadLocal.setSurname(apiContext.getSurname());
        apiContextThreadLocal.setCompanyId(apiContext.getCompanyId());
        apiContextThreadLocal.setCompanyName(apiContext.getCompanyName());


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ApiContext.unset();
    }
}
