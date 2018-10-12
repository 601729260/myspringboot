import org.junit.Before;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2018/7/9
 * Time           :   下午7:51
 * Description    :
 */
public class TestGenerator {

    private File configFile;


    @Before
    public void before() {
        //读取mybatis参数
        configFile = new File("/Users/wangguannan/IdeaProjects/myspringboot/src/main/resources/generatorConfig.xml");
        }

    @Test
    public void test() throws Exception{
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        Properties properties=System.getProperties();
        properties.put("index",2);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }


}