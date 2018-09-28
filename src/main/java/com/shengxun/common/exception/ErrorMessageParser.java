package com.shengxun.common.exception;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.shengxun.common.exception.annotation.Error;
import com.shengxun.common.exception.annotation.ErrorComponent;
import com.shengxun.common.exception.annotation.Errors;

/**
 * errorMessage.xml 解析
 * errorMessage.xml 格式：
 * <errors>
 *      <error>
 *          <code>ums-601</code>
 *          <message>参数格式不对</code>
 *      <error>
 *      <error>
 *          <code>ums-602</code>
 *          <message>手机格式不对</code>
 *      <error>
 * </errors>
 * @author E0446
 *
 */
@Component
public class ErrorMessageParser implements InitializingBean, ApplicationContextAware{

    private static final Logger logger = LoggerFactory.getLogger(ErrorMessageParser.class);

    private Map<String,String> msgMap = new HashMap<String,String>();
    private String modelName;
    
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        parse();
    }

    @SuppressWarnings("unchecked")
    private void parse(){
        parseFile();
    }

    private void parseFile(){
        SAXReader reader =null;
        Document document = null;
        try{
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config/errorMessage.xml");
            if(inputStream != null){
                reader = new SAXReader(); 
                document = reader.read(inputStream);
                Element node = document.getRootElement();  
                Map<String,String> tempMsg = new HashMap<>(tableSizeFor((int)(node.nodeCount()*1.3)));
                Iterator<Element> it = node.elementIterator(); 
                Element subNode = null;
                while(it.hasNext()){
                    subNode = it.next();
                    tempMsg.put(subNode.elementText("code"), subNode.elementText("message"));
                }
                msgMap.putAll(tempMsg);
            }
        } catch (DocumentException e) {
            logger.error("loading errorMessage.xml error.", e);
        } catch (Exception e1){
            logger.error("loading errorMessage.xml error.", e1);
        }
    }
    
    public String getMessage(String code){
        return msgMap.get(code);
    }

    public String getModelName(){
        return modelName;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // TODO Auto-generated method stub
        Map<String, ErrorMessage> map = applicationContext.getBeansOfType(ErrorMessage.class);
        if(map.isEmpty()){
            return;
        }
        Iterator<Entry<String, ErrorMessage>> it = map.entrySet().iterator();
        while(it.hasNext()){
            Entry<String, ErrorMessage> entry = it.next();
            ErrorMessage errorMessage = entry.getValue();
            ErrorComponent[] errorComponentArray = errorMessage.getClass().getAnnotationsByType(ErrorComponent.class);
            ErrorComponent errorComponent = errorComponentArray[0];
            String modelName = errorComponent.model();
            Method[] methods = errorMessage.getClass().getDeclaredMethods();
            for(Method method : methods){
                Errors[] errorsArray = method.getAnnotationsByType(Errors.class);
                if(errorsArray == null || errorsArray.length==0){
                    return;
                }
                Error[] errorArray = null;
                String code = null;
                for(Errors errors: errorsArray){
                    errorArray = errors.value();
                    Map<String,String> tempMsg = new HashMap<>(tableSizeFor((int)(errorArray.length*1.3)));
                    for(Error error:errorArray){
                        code = error.code();
                        if(error.withPrefix()){
                            code = modelName+'.'+code;
                        }
                        tempMsg.put(code, error.message());
                    }
                    msgMap.putAll(tempMsg);
                }
            }
            this.modelName = modelName;
        }
    }
    
    
    private int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : n + 1;
    }
}
