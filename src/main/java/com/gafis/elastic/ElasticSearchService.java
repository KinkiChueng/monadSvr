package com.gafis.elastic;

import com.gafis.elastic.Jackson.ESUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.gafis.util.CommonUtils;
import org.springframework.web.bind.annotation.RestController;
import java.net.*;
import java.util.List;
import java.util.Map;

/**
 * Created by lasia on 2017/8/21.
 */
@RestController
@RequestMapping("/elastic")
public class ElasticSearchService {
    private Settings settings;
    private TransportClient client;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 初始化配置
     * @throws Exception
     */
    public void init() throws Exception {
        settings = Settings.settingsBuilder()
                //指定集群名称
                .put("cluster.name", CommonUtils.GetConfigInfo().get("elasticSearch.cluster.name").toString())
                //探测集群中机器状态
                .put("client.transport.sniff", true).build();
        client = TransportClient.builder().settings(settings).build();
        try {
            String ip = CommonUtils.GetConfigInfo().get("elasticSearch.ip").toString();
            int port = Integer.valueOf(CommonUtils.GetConfigInfo().get("elasticSearch.port").toString());
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
        } catch (UnknownHostException e) {
            throw new Exception("elastic Search初始化失败" + e.getMessage());
        }
    }

    /**
     * 遍历调用上传
     * @param personList
     * @throws Exception
     */
    public void OperElastic(List<Map<String, Object>> personList) throws Exception {
        try {
            init();
            for (Map<String, Object> person : personList) {
                String personid = person.get("PERSONID") == null ? "" : person.get("PERSONID").toString();
                String jsonPerson = ESUtils.toJson(person);
                InsertElastic(CommonUtils.GetConfigInfo().get("elasticSearch.indexName").toString(),
                        CommonUtils.GetConfigInfo().get("elasticSearch.type").toString(), personid, jsonPerson);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            client.close();
        }
    }

    /**
     * 上传索引，新建索引
     * @param indexName
     * @param type
     * @param personid
     * @param jsonPerson
     * @throws Exception
     */
    public void InsertElastic(String indexName, String type, String personid, String jsonPerson) throws Exception {
        if (ExistElastic(indexName)) {
            IndexResponse response = client.prepareIndex(indexName, type)
                    .setId(personid)
                    .setSource(jsonPerson)
                    .execute()
                    .actionGet();
            //多次index这个版本号会变
            logger.info(personid + " 更新次数，response.version():" + response.getVersion());
        } else {
            throw new Exception("该索引不存在，请先建索引");
        }
    }

    /**
     * 删除索引
     * @param deleteName
     */
    @RequestMapping("/delete/{deleteName}")
    public String DeleteElastic(@PathVariable("deleteName") String deleteName) {
        try {
            init();
            if (ExistElastic(deleteName)) {
                DeleteIndexResponse response = client.admin().indices().prepareDelete(deleteName).execute().actionGet();
                logger.info(" 成功删除: " + deleteName);
                if (null != client) {
                    client.close();
                }
            }
        } catch (Exception e) {
            logger.error("出现错误:错误信息:{},堆栈信息:{}", e.getMessage(), e.getStackTrace());
        }
        return " 该索引已成功删除： " + deleteName;
    }

    /**
     * 判断索引是否存在
     * @param indexName
     * @return
     */
    public Boolean ExistElastic(String indexName) throws Exception {
        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(new IndicesExistsRequest(indexName)).actionGet();
        return inExistsResponse.isExists();
    }

    /**
     * 新建索引
     * @param indexName
     */
    @RequestMapping("/index/{indexName}")
    public String IndexElastic(@PathVariable("indexName") String indexName) {
        try {
            init();
            if (!ExistElastic(indexName)) {
                CreateIndexResponse response = client.admin().indices().prepareCreate(indexName).execute().actionGet();
            } else {
                logger.info(" 该索引已存在： " + indexName);
                return " 该索引已存在： " + indexName;
            }
        } catch (Exception e) {
            logger.error("出现错误:错误信息:{},堆栈信息:{}", e.getMessage(), e.getStackTrace());
        }
        return " 该索引已成功创建： " + indexName;
    }
}

