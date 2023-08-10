package com.hasangurbuz.vehiclemanager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.api.ApiExceptionCode;
import com.hasangurbuz.vehiclemanager.domain.UserRole;
import org.apache.http.entity.ContentType;
import org.openapitools.model.ErrorDTO;
import org.openapitools.model.XUserDTO;
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
        XUserDTO user = this.authenticatedUser(request);

        if (user == null) {
            ErrorDTO error = new ErrorDTO();
            error.setCode(ApiExceptionCode.AUTH_ERROR);
            error.setMessage("Auth error");
            response.setContentType(ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8).toString());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");

            try {
                response.getWriter().println(objectMapper.writeValueAsString(error));
            } catch (Exception e) {
                //log.error("Error on response.write", e);
            }
            return false;
        }

        ApiContext apiCtx = ApiContext.create();

        apiCtx.setUserId(user.getUserId());
        apiCtx.setUserRole(UserRole.valueOf(user.getUserRole().getValue()));
        apiCtx.setName(user.getName());
        apiCtx.setSurname(user.getSurname());
        apiCtx.setCompanyId(user.getCompanyId());
        apiCtx.setCompanyName(user.getCompanyName());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ApiContext.unset();
    }

    private XUserDTO authenticatedUser(HttpServletRequest request) {
        try {
            String header = request.getHeader("X-User");
            XUserDTO user = objectMapper.readValue(header, XUserDTO.class);
            if (user.getUserId() == null) {
                return null;
            }

            if (user.getCompanyId() == null) {
                return null;
            }

            if (user.getUserRole() == null) {
                return null;
            }

            return user;
        } catch (Exception e) {
            return null;
        }
    }
}
