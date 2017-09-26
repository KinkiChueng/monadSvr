package com.gafis.elastic.Jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * 将查出的人员信息结果转为json
 * Created by lasia on 2017/8/20.
 */
public class ESUtils {

    public static String toJson(Map<String,Object> o) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new Exception(e.getMessage());
        }
    }
}
