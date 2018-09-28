package com.shengxun.common.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.shengxun.common.exception.BizException;
import com.shengxun.common.util.EncriptUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author E0446
 */

@Slf4j
@Component
public class QueryClient {

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private HttpClientConfig httpClientConfig;
    
    @Value("${queryclient.connectTimeout}")
    private String connectionTimeout;

    @Value("${queryclient.socketTimeout}")
    private String socketTimeout;
    @Value("${queryclient.connectionRequestTimeout}")
    private String connectionRequestTimeout;

    public <T> T get(String url, Map<String, String> params, TypeReference<T> clazz) {
        HttpGet httpGet = new HttpGet(urlParamterStringer(getRealUrl(url), params));
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(Integer.valueOf(socketTimeout)).setConnectTimeout(Integer.valueOf(connectionTimeout)).setConnectionRequestTimeout(Integer.valueOf(connectionRequestTimeout)).build();
        httpGet.setConfig(requestConfig);
        String json = null;
        CloseableHttpResponse response = null;
        try {
            log.info("发出请求url: "+httpGet.getURI());
            response = httpClient.execute(httpGet);
            InputStream in = response.getEntity().getContent();
            json = IOUtils.toString(in);
            in.close();
        } catch (UnsupportedOperationException | IOException e) {
            throw new BizException("406", "系统异常",e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSON.parseObject(json, clazz);
    }

    public <T> T restGet(String url, Map<String, String> params, TypeReference<T> clazz) {
        HttpGet httpGet = new HttpGet(restUrlParamterStringer(getRealUrl(url), params));
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(Integer.valueOf(socketTimeout)).setConnectTimeout(Integer.valueOf(connectionTimeout)).setConnectionRequestTimeout(Integer.valueOf(connectionRequestTimeout)).build();
        httpGet.setConfig(requestConfig);
        String json = null;
        CloseableHttpResponse response = null;
        try {
            log.info("发出请求url: "+httpGet.getURI());
            response = httpClient.execute(httpGet);
            InputStream in = response.getEntity().getContent();
            json = IOUtils.toString(in);
            in.close();
        } catch (UnsupportedOperationException | IOException e) {
            throw new BizException("406", "系统异常",e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSON.parseObject(json, clazz);
    }

    public <T> T get(String url, Object object, TypeReference<T> clazz) {

        HttpGet httpGet = new HttpGet(urlObjectStringer(getRealUrl(url), object));
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(Integer.valueOf(socketTimeout)).setConnectTimeout(Integer.valueOf(connectionTimeout)).setConnectionRequestTimeout(Integer.valueOf(connectionRequestTimeout)).build();
        httpGet.setConfig(requestConfig);
        String json = null;
        CloseableHttpResponse response = null;
        try {
            log.info("发出请求url: "+httpGet.getURI());
            response = httpClient.execute(httpGet);
            InputStream in = response.getEntity().getContent();
            json = IOUtils.toString(in);
            in.close();
        } catch (UnsupportedOperationException | IOException e) {
            throw new BizException("406", "系统异常",e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSON.parseObject(json, clazz);
    }

    public void post(String url, Map<String, String> params) {
        post(url, params, null);
    }

    public <T> T post(String url, Map<String, String> params, TypeReference<T> clazz) {
        HttpPost httpPost = new HttpPost(getRealUrl(url));
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(Integer.valueOf(socketTimeout)).setConnectTimeout(Integer.valueOf(connectionTimeout)).setConnectionRequestTimeout(Integer.valueOf(connectionRequestTimeout)).build();
        httpPost.setConfig(requestConfig);
        StringEntity entity = new StringEntity(JSON.toJSONString(params), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        String json = null;
        CloseableHttpResponse response = null;
        try {
            log.info("发出请求url: "+httpPost.getURI().toURL());
            response = httpClient.execute(httpPost);
            InputStream in = response.getEntity().getContent();
            json = IOUtils.toString(in);
            in.close();
        } catch (UnsupportedOperationException | IOException e) {
            throw new BizException("406", "系统异常", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (clazz == null)
            return null;
        return JSON.parseObject(json, clazz);
    }

    public Integer postToStatus(String url, JSONObject params) {
        HttpPost httpPost = new HttpPost(getRealUrl(url));
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(Integer.valueOf(socketTimeout)).setConnectTimeout(Integer.valueOf(connectionTimeout)).setConnectionRequestTimeout(Integer.valueOf(connectionRequestTimeout)).build();
        httpPost.setConfig(requestConfig);
        StringEntity entity = new StringEntity(JSON.toJSONString(params), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = null;
        Integer status = 500;
        try {
            log.info("发出请求url: "+httpPost.getURI().toURL());
            response = httpClient.execute(httpPost);
            status = response.getStatusLine().getStatusCode();
        } catch (UnsupportedOperationException | IOException e) {
            throw new BizException("406", "系统异常", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return status;
    }

    public Integer deleteToStatus(String url, JSONObject params) {
        HttpDelete httpDelete = new HttpDelete(urlObjectStringer(getRealUrl(url), params));
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(Integer.valueOf(socketTimeout)).setConnectTimeout(Integer.valueOf(connectionTimeout)).setConnectionRequestTimeout(Integer.valueOf(connectionRequestTimeout)).build();
        httpDelete.setConfig(requestConfig);
        StringEntity entity = new StringEntity(JSON.toJSONString(params), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");

        CloseableHttpResponse response = null;
        Integer status = 500;
        try {
            log.info("发出请求url: "+httpDelete.getURI().toURL());
            response = httpClient.execute(httpDelete);
            status = response.getStatusLine().getStatusCode();
        } catch (UnsupportedOperationException | IOException e) {
            throw new BizException("406", "系统异常", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return status;
    }

    public <T> T getFromCRM(String url, LinkedHashMap<String, String> params, TypeReference<T> clazz) {
        StringBuilder urlbf = new StringBuilder(getRealUrl(url)).append("/?");
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        String appSecret = null;
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null) {
                continue;
            }
            if ("appSecret".equals(key)) {
                appSecret = value;
                continue;
            }
            urlbf.append(key).append("=").append(value).append("&");
        }
        String token = EncriptUtil.encriptByMd5(params.get("timestamp").toString(), params.get("appid"), appSecret);
        CloseableHttpResponse response = null;
        String json = null;
        try {
            urlbf.append("token").append("=").append(token);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(Integer.valueOf(socketTimeout)).setConnectTimeout(Integer.valueOf(connectionTimeout)).setConnectionRequestTimeout(Integer.valueOf(connectionRequestTimeout)).build();
            HttpGet httpGet = new HttpGet(urlbf.toString());
            httpGet.setConfig(requestConfig);
            log.info("发出请求url: "+httpGet.getURI());
            response = httpClient.execute(httpGet);
            InputStream in = response.getEntity().getContent();
            JSONObject object = JSON.parseObject(in, JSONObject.class);
            String error = object.getString("error");
            if (error != null) {
                log.error("query internal error: {}", error);
                throw new BizException("500", String.format("query internal error: %s", error));
            }
            json = object.get("data").toString();
            in.close();
        } catch (UnsupportedOperationException | IOException e) {
            throw new BizException("406", "系统异常",e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSON.parseObject(json, clazz);
    }

    public <T> T post(String url, Object object, TypeReference<T> clazz) {
        HttpPost httpPost = new HttpPost(getRealUrl(url));
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(Integer.valueOf(socketTimeout)).setConnectTimeout(Integer.valueOf(connectionTimeout)).setConnectionRequestTimeout(Integer.valueOf(connectionRequestTimeout)).build();
        httpPost.setConfig(requestConfig);
        StringEntity entity = new StringEntity(JSON.toJSONString(object), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        String json = null;
        CloseableHttpResponse response = null;
        try {
            log.info("发出请求url: "+httpPost.getURI().toURL());
            response = httpClient.execute(httpPost);
            InputStream in = response.getEntity().getContent();
            json = IOUtils.toString(in);
            in.close();
        } catch (UnsupportedOperationException | IOException e) {
            throw new BizException("406", "系统异常", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return JSON.parseObject(json, clazz);
    }

    public <T> T uploadFile(String url, MultipartFile file, TypeReference<T> clazz) {
        HttpPost httpPost = new HttpPost(getRealUrl(url));
        String fileName = file.getOriginalFilename();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        String json = null;
        CloseableHttpResponse response = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(Integer.valueOf(socketTimeout)).setConnectTimeout(Integer.valueOf(connectionTimeout)).setConnectionRequestTimeout(Integer.valueOf(connectionRequestTimeout)).build();
            httpPost.setConfig(requestConfig);
            builder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
            builder.addTextBody("filename", fileName);// 类似浏览器表单提交，对应input的name和value
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            log.info("发出请求url: "+httpPost.getURI().toURL());
            response = httpClient.execute(httpPost);
            InputStream in = response.getEntity().getContent();
            json = IOUtils.toString(in);
            in.close();
        } catch (UnsupportedOperationException | IOException e) {
            throw new BizException("406", "系统异常");
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSON.parseObject(json, clazz);
    }

    private <K, V> String urlParamterStringer(String urlPrefix, Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            return urlPrefix;
        }
        int capacity = map.size() * 30;
        StringBuilder buffer = new StringBuilder(capacity);
        buffer.append(urlPrefix).append('?');
        Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
        boolean firstElement = true;
        while (it.hasNext()) {
            Map.Entry<K, V> entry = it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if(!firstElement){
                buffer.append("&");
            }else{
                firstElement=false;
            }
            buffer.append(key);
            buffer.append('=');
            buffer.append(value);
        }
        return buffer.toString();
    }

    private <K, V> String restUrlParamterStringer(String url, Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            return url;
        }
        Pattern pattern = Pattern.compile("\\{\\w*\\}");
        Matcher matcher = pattern.matcher(url);
        Set<String> pathParamKey = new HashSet<>(16);
       while(matcher.find()){
            String param = matcher.group();
            pathParamKey.add(param.substring(1,param.length()-1));
        }
        StringBuilder sb = new StringBuilder(64);
        sb.append("?");
        boolean firstElement = true;
        Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> entry = it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (value == null) {
                continue;
            }
            if(pathParamKey!=null&&pathParamKey.contains(key)){
                url = url.replaceAll("\\{" + key + "\\}", value);
            }
            else
            {
                if(firstElement){
                    firstElement=false;
                }else{
                    sb.append("&");
                }
                sb.append(key).append("=").append(value);
            }
        }
        return url+sb.toString();
    }

    private <K, V> String urlObjectStringer(String urlPrefix, Object object) {
        if (object == null) {
            return urlPrefix;
        }
        StringBuilder buffer = new StringBuilder(128);
        buffer.append(urlPrefix).append('?');
        JSONObject jsonObject = (JSONObject) JSON.toJSON(object);
        Iterator<Map.Entry<String, Object>> iterator = jsonObject.entrySet().iterator();
        boolean firstElement = true;
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if(!firstElement){
                buffer.append("&");
            }else{
                firstElement=false;
            }
            buffer.append(key);
            buffer.append('=');
            buffer.append(value);
        }
        return buffer.toString();
    }

    private String getRealUrl(String url) {

        String server = url.substring(0, url.indexOf('/'));
        String urlPrefix = httpClientConfig.getServers().get(server);
        StringBuilder sb = new StringBuilder(128);
        if (urlPrefix.endsWith("/")) {
            sb.append(urlPrefix);
        } else {
            sb.append(urlPrefix).append("/");
        }
        if (!httpClientConfig.getServerPrefix().get(server)) {
            sb.append(url.substring(url.indexOf('/') + 1));
        } else {
            sb.append(url);
        }
        return sb.toString();
    }

}

