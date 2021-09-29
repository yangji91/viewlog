package com.codeisrun.viewlog;

import com.codeisrun.viewlog.bean.LogInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ViewlogApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewlogApplicationTests.class);

    @Test
    public void contextLoads() {
    }

    @Test
    public void test() {
        LogInfo logInfo = new LogInfo();
        logInfo.setLogPath("D:\\var\\logs");
        String aaa = logInfo.getLatestFile();
        LOGGER.info(aaa);
    }

}
