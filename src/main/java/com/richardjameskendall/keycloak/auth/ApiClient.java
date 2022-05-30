/*
 * Copyright Richard Kendall 2022
 *
 */

package com.richardjameskendall.keycloak.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ApiClient {
    public static Object PostJsonToApi(String url, String token, Map<String, Object> body) throws IOException {
        // get JSON
        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(body);

        // make request
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        HttpEntity bodyEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
        httpPost.setEntity(bodyEntity);
        httpPost.setHeader("Authorization",  "Bearer " + token);
        httpPost.setHeader("ConsistencyLevel", "eventual");

        // get response
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        Object o = gson.fromJson(responseBody, Object.class);
        return o;
    }

    public static Object GetToApi(String url, String token) throws IOException {
        // make request
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(URIUtil.encodeQuery(url));
        httpGet.setHeader("Authorization",  "Bearer " + token);
        httpGet.setHeader("ConsistencyLevel", "eventual");

        // get response
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        Gson gson = new GsonBuilder().create();
        Object o = gson.fromJson(responseBody, Object.class);
        return o;
    }
}
