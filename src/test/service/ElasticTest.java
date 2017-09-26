import com.gafis.SpringbootMyBatisApplication;
import com.gafis.elastic.ElasticSearchService;
import com.gafis.elastic.Jackson.ESUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.gafis.util.CommonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lasia on 2017/8/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringbootMyBatisApplication.class) // 指定我们SpringBoot工程的Application启动类
public class ElasticTest {

    @Autowired
    ElasticSearchService elasticSearchService;

    @Test
    public void InsertElasticTest() throws Exception {
//        Person person = new Person();
//        person.setPersonid("87654321");
//        person.setAddress("M城");
//        person.setAliasname("小八8");
//        person.setAssist_level("kfnalfdnla");
        Map<String, Object> person = new HashMap();
        elasticSearchService.init();
        person.put("PERSONID","5");
        person.put("ADDRESS","aaa");
        elasticSearchService.InsertElastic(CommonUtils.GetConfigInfo().get("elasticSearch.indexName").toString(),
                CommonUtils.GetConfigInfo().get("elasticSearch.type").toString(), "22", ESUtils.toJson(person));
    }

    @Test
    public void DeleteElasticTest() throws Exception {
        elasticSearchService.init();
        elasticSearchService.DeleteElastic("sssaa");
    }

    @Test
    public void IndexElasticTest() throws Exception {
        elasticSearchService.init();
        elasticSearchService.IndexElastic("k");
    }

//    @Test
//    public void UpdateElasticTest() throws Exception {
//        ElasticSearchService elasticSearchService = new ElasticSearchService();
//        Person person = new Person();
//        person.setPersonid("1234567");
//        person.setAddress("C城");
//       // elasticSearchService.UpdateElastic("1234567", ESUtils.toJson(person));
//    }
//
//    @Test
//    public void ExistElasticTest() throws Exception {
//        ElasticSearchService elasticSearchService = new ElasticSearchService();
//       // System.out.print(elasticSearchService.ExistElastic("1234567"));
//    }
//
//    @Test
//    public void GetElasticTest() throws Exception {
//        ElasticSearchService elasticSearchService = new ElasticSearchService();
//        Person person = new Person();
//        person.setPersonid("1234567");
//        person.setAddress("C城");
//      //  elasticSearchService.GetElastic("1234567");
//    }

}
