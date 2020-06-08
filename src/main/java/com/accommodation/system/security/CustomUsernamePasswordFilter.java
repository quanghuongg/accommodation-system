package com.accommodation.system.security;

import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.uitls.ServiceUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomUsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String input = null;

        try {
            input = ServiceUtils.getStringStream(request.getInputStream(), request.getContentLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map authenData = new HashMap();

        try {
            authenData = objectMapper.readValue(input, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String username = authenData.get("username").toString();
        String password = authenData.get("password").toString();

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String token = jwtTokenProvider.generateToken(authResult);
        Response responseObject = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(token)
                .build();

        responseToClient(new JSONObject(responseObject).toString(), response);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        Response responseObject = Response.builder()
                .code(Constant.FAILED_CODE)
                .message(Constant.LOGIN_FAIL)
                .data(null)
                .build();

        responseToClient(new JSONObject(responseObject).toString(), response);
    }

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    private void responseToClient(String message, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().write(message.getBytes("UTF-8"));
        response.getOutputStream().close();
    }
}
