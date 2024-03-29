package com.accommodation.system.uitls;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class RestUtil {
    public static String doGet(String url) throws IOException {

        HttpGet httpGet = new HttpGet(url);

        httpGet.setHeader(HttpHeaders.ACCEPT, "application/json");
        httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        CloseableHttpResponse response = HttpClients.createDefault().execute(httpGet);

        if (response.getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        }

        httpGet.releaseConnection();
        return null;
    }

    public static String doPost(String url, Object data, Map<String, String> headers) throws IOException {

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        for (String key : headers.keySet()) {
            httpPost.setHeader(key, headers.get(key));
        }

        if (ServiceUtils.isNotEmpty(data)) {
            String body = JsonUtil.toJsonString(data);
            log.info("request body " + data);
            StringEntity encodedFormEntity = new StringEntity(body, "UTF-8");
            httpPost.setEntity(encodedFormEntity);
        }

        log.info("call " + url);
        CloseableHttpResponse response = HttpClients.createDefault().execute(httpPost);
        log.info("response status " + response.getStatusLine().getStatusCode());

        if (response.getStatusLine().getStatusCode() == 200) {
            String body = EntityUtils.toString(response.getEntity(), "UTF-8");
            log.info("response body:" + body);
            return body;
        }
        httpPost.releaseConnection();
        return null;
    }
}
