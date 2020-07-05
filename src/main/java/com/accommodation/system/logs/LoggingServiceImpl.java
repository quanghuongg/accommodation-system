package com.accommodation.system.logs;

import com.accommodation.system.define.Constant;
import com.accommodation.system.uitls.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Component
public class LoggingServiceImpl implements LoggingService {

    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body) throws JsonProcessingException {
        Map<String, String> parameters = buildParametersMap(httpServletRequest);

        String[] uuid = UUID.randomUUID().toString().split("-");

        String referenceId = uuid[uuid.length - 1];
        httpServletRequest.setAttribute("start_time", System.currentTimeMillis());
        httpServletRequest.setAttribute("reference_id", referenceId);

        WriteLog.info("[REQ][" + referenceId + "] " + httpServletRequest.getMethod() + "\t" + httpServletRequest.getRequestURI());
        WriteLog.info("[REQ][" + referenceId + "] " + "headers=[" + buildHeadersMap(httpServletRequest) + "]");
        WriteLog.info("[REQ][" + referenceId + "] " + "body=" + JsonUtil.toJsonString(body));
        WriteLog.info("[REQ][" + referenceId + "] " + "user_id=" + httpServletRequest.getAttribute("user_id") + "\n");
    }

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) throws JsonProcessingException {
        String referenceId = String.valueOf(httpServletRequest.getAttribute("reference_id"));
        WriteLog.info("[RES][" + referenceId + "] " + "status=[" + httpServletResponse.getStatus() + "]");
        WriteLog.info("[RES][" + referenceId + "] " + "headers=[" + buildHeadersMap(httpServletResponse) + "]");

        boolean ignore = false;
        String[] regexs = Constant.Logging.LOGGING_IGNORE;
        for (int i = 0; i < regexs.length; i++) {
            String regex = regexs[i];
            if (!"".equals(regex) && httpServletRequest.getRequestURI().contains(regex)) {
                ignore = true;
            }
        }

        Object startTime = httpServletRequest.getAttribute("start_time");
        String durationString = "";
        if (null != startTime) {
            durationString = " process in " + (System.currentTimeMillis() - (long) startTime) + " ms";
        }
        WriteLog.info("[RES][" + referenceId + "] " + "body=" + (ignore ? "******" : JsonUtil.toJsonString(body)) + durationString);
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }

        return map;
    }
}