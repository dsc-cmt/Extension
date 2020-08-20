package test;

import io.github.cmt.extension.consumer.service.TestService;
import io.github.cmt.extension.consumer.Application;
import io.github.cmt.extension.common.BusinessContext;
import io.github.cmt.extension.common.util.ApplicationContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author shengchaojie
 * @date 2019-10-21
 **/
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class ApplicationContextHolderTest {

    @Autowired
    private TestService testService;

    @Test
    public void test(){
        ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
        Assert.assertNotNull(ApplicationContextHolder.getApplicationContext());
    }

    @Test
    public void testSpi(){
        BusinessContext.setBizCode("a");
        String result = testService.hello();
        Assert.assertTrue("HelloA".equals(result));

        BusinessContext.setBizCode("b");
        result = testService.hello();
        Assert.assertTrue("helloB".equals(result));

        BusinessContext.setBizCode("c");
        result = testService.hello();
        Assert.assertTrue("helloC".equals(result));

        BusinessContext.setBizCode("d");
        result = testService.hello();
        Assert.assertTrue("helloD".equals(result));

        BusinessContext.setBizCode("f");
        result = testService.hello();
        Assert.assertTrue("helloF".equals(result));

        BusinessContext.setBizCode("default");
        result = testService.hello();
        Assert.assertTrue("helloD".equals(result));

        BusinessContext.setBizCode("notexist");
        result = testService.hello();
        Assert.assertTrue("helloD".equals(result));
    }


}
