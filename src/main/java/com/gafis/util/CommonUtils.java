package com.gafis.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;


import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import java.rmi.RemoteException;


/**
 * 公用的工具类
 *
 * @author yuchen
 */
public class CommonUtils {

    private static HashMap<String, Object> configMap = new HashMap();

    public static HashMap<String, Object> GetConfigInfo() throws Exception {
        try {
            if (null == configMap || configMap.size() == 0) {
                configMap.put("look", getPropertiesProp("look"));
                configMap.put("endpoint", getPropertiesProp("endpoint"));
                configMap.put("qName", getPropertiesProp("qName"));
                configMap.put("rowNum", getPropertiesProp("rowNum"));
                configMap.put("queryName", getPropertiesProp("queryName"));
                configMap.put("elasticSearch.ip", getPropertiesProp("elasticSearch.ip"));
                configMap.put("elasticSearch.port", getPropertiesProp("elasticSearch.port"));
                configMap.put("elasticSearch.cluster.name", getPropertiesProp("elasticSearch.cluster.name"));
                configMap.put("elasticSearch.indexName", getPropertiesProp("elasticSearch.indexName"));
                configMap.put("elasticSearch.type", getPropertiesProp("elasticSearch.type"));
            }
        } catch (Exception ex) {
            throw new Exception("从内存中获得配置信息时出错:" + ex.getMessage());
        }
        return configMap;
    }

    /**
     * 读取配置文件
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String getPropertiesProp(String key) throws Exception {


        Properties prop = new Properties();
        String value;
        try {
            InputStream is = CommonUtils.class.getClass().getResourceAsStream("/application.properties");
            prop.load(is);
            value = prop.getProperty(key);

        } catch (Exception e) {
            throw new Exception("读取配置文件错误:" + e.getMessage());
        }
        return value;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean StringNullOrEmpty(String str) {
        boolean bstr = false;
        if (StringUtils.isEmpty(str) || StringUtils.isBlank(str)) {
            bstr = true;

        }
        return bstr;
    }

    /**
     * 远程过程调用，client-server 客户端调用webservice
     */
    public static String invokeRemoteRPC(String person, String seq, String funName)
            throws Exception {
        String endpoint = GetConfigInfo().get("endpoint").toString();
        String result = "调用没有返回结果";
        Service service = new Service();
        Call call;
        Object[] object = new Object[2];
        object[0] = person;
        object[1] = seq;
        try {
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(endpoint);// 远程调用路径
            call.setOperationName(new QName(GetConfigInfo().get("qName").toString(), funName));// 调用的方法名
            call.addParameter("paramXML", XMLType.XSD_STRING, ParameterMode.IN);
            // 设置返回值类型：
            call.setTimeout(15000);//设置超时时限
            call.setReturnType(XMLType.XSD_STRING);// 返回值类型：String
            result = (String) call.invoke(object);// 远程调用
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        } catch (RemoteException e) {
            throw new RemoteException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return result;
    }

}
