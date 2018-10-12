package wgn.myspringboot.datasource;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.spring.annotation.MapperScan;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;
import wgn.myspringboot.mybatis.IMapper;

import java.io.IOException;
import java.util.Properties;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2018/10/10
 * Time           :   下午5:21
 * Description    :
 */
@Configuration
public class DataSourceConfig {

    /**
     * druid 公共配置
     */
    @Bean
    @ConditionalOnMissingBean
    public IDataSourceProperties dataSourceProperties() {
        return new DefaultDatasourceProperties();
    }




}