package com.aee.service.exception;

import com.aee.service.payload.response.BaseResponse;
import com.aee.service.security.jwt.AuthEntryPointJwt;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException, ServletException {
        logger.error("403 error: {}", exc.getMessage());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setData("Forbidden");
        String baseResponseString = new Gson().toJson(baseResponse);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(baseResponseString);
        out.flush();
    }
}
