package com.gafis.sync;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.gafis.elastic.ElasticSearchService;
import com.gafis.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import com.gafis.util.CommonUtils;


@Configuration
@EnableScheduling
@Controller
@PropertySource("classpath:application.properties")
public class SyncTask {

    @Autowired
    IndexService indexService;
    @Autowired
    ElasticSearchService elasticService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "${jobs.schedule}")
    public void scheduler() throws Exception {
        String look = CommonUtils.GetConfigInfo().get("look").toString();
        try {
            if (look.equals("1")) {
                cronData();
            }
        } catch (Exception ex) {
            logger.error("出现错误:错误信息:{},堆栈信息:{}", ex.getMessage(), ex.getStackTrace());
        }
    }

    private void cronData() throws Exception {
        logger.info("同步完成情况-定时任务执行中......");
        long minSeq = indexService.getMinSeq();
        List<Map<String, Object>> personList = indexService.getPersonBySeq(minSeq);
        long lastSeq = ((BigDecimal) personList.get(personList.size() - 1).get("SEQ")).longValue();
        if (lastSeq != minSeq) {
            if (minSeq >= 0) {
                elasticService.OperElastic(personList);         //建索引

                Boolean status = indexService.updateSeq(lastSeq);       //更改seq状态
                if (status == true) {
                    logger.info("seq更新成功：" + lastSeq);
                }

            }
        }

    }
}
