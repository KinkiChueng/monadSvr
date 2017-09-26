import com.gafis.SpringbootMyBatisApplication;
import com.gafis.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;
import java.util.Map;

/**
 * Created by lasia on 2017/8/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringbootMyBatisApplication.class) // 指定我们SpringBoot工程的Application启动类
public class IndexServiceTest {
    @Autowired
    IndexService indexService;

    @Test
    public void getMinSeqTest() throws Exception {
        System.out.println("当前最小的seq值为" + indexService.getMinSeq());
    }

    @Test
    public void getPersonBySeqTest() throws Exception {
        List<Map<String,Object>> personList = indexService.getPersonBySeq(1L);
        for (Map<String,Object> person : personList) {
            System.out.println(person);
        }
    }

    /**
     * 更新seq慎用
     */
    @Test
    public void updateSeqTest() throws Exception {
        System.out.println("更新seq   " + indexService.updateSeq(1L));
    }
}
