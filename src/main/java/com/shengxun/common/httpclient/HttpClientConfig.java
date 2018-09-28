package com.shengxun.common.httpclient;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 
 * @author E0446
 *
 */
@ConfigurationProperties
@Configuration
public class HttpClientConfig implements InitializingBean{

    @Value("${service.refer}")
    private String serviceReference =null;
    
    private Map<String,String> servers =new HashMap<>();
    private Map<String,Boolean> serverWithPrefix = new HashMap<>();
    
    @Autowired private Environment env;
    
    public Map<String,String> getServers(){
        return servers;
    }
    
    public Map<String,Boolean> getServerPrefix(){
        return serverWithPrefix;
    }
    private void init(){
        String[] refers = serviceReference.split(",");
        for(String refer:refers){
            String[] referItem = refer.split(":");
            String server = referItem[0];
            Boolean withPrefix = referItem[1].equals("1")?Boolean.TRUE:Boolean.FALSE;
            serverWithPrefix.put(server, withPrefix);
            String url = env.getProperty("service."+server);
            servers.put(server, url);
        }
    }
    
    private PoolingHttpClientConnectionManager createConnectionManager() {
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
        return cm;
    }

    @Bean("httpClient")
    public CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(createConnectionManager()).build();
        return httpClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        init();
    }
}
