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
 * Date           :   2018/10/11
 * Time           :   下午8:06
 * Description    :
 */
@Configuration
public class SlaveDataSourceConfig {



    @Bean
    @ConditionalOnMissingBean
    public IDataSourceProperties dataSourceProperties() {
        return new DefaultDatasourceProperties();
    }





    @Primary
    @Bean(name="businessDataSource")
    public AtomikosDataSourceBean businessDataSource(IDataSourceProperties properties) throws IOException {
        return getAtomikosDataSourceBean(properties.getDataSourceItems().get(2));
    }

   private AtomikosDataSourceBean getAtomikosDataSourceBean(DataSourceItem dataSourceItem) throws IOException {

        AtomikosDataSourceBean dataSourceBean = new AtomikosDataSourceBean();
        // 配置DruidXADataSource
        DruidXADataSource xaDataSource = new DruidXADataSource();

        Properties properties=new Properties();

        properties.setProperty("druid.name",dataSourceItem.getName());
        properties.setProperty("druid.url",dataSourceItem.getUrl());
        properties.setProperty("druid.username",dataSourceItem.getUsername());
        properties.setProperty("druid.password",dataSourceItem.getPassword());

        xaDataSource.configFromPropety(properties);
        // 设置置AtomikosDataSourceBean XADataSource
        dataSourceBean.setXaDataSource(xaDataSource);
        dataSourceBean.setUniqueResourceName("businessDataSource");
        return dataSourceBean;
    }

   /*
    @Bean(name="businessSqlSessionFactory")
    public SqlSessionFactory businessSqlSessionFactoryBean(@Qualifier("businessDataSource") AtomikosDataSourceBean businessDataSource) throws Exception{
        return getSqlSessionFactoryBean(businessDataSource);
    }

    private SqlSessionFactory getSqlSessionFactoryBean(AtomikosDataSourceBean dataSource) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources(CLASSPATH_MAPPER_XML));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sqlSessionFactoryBean.getObject();
    }







    @Bean(name = "slaveSqlSessionTemplate")
    public SqlSessionTemplate slaveSqlSessionTemplate(
            @Qualifier("businessSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Bean
    public MapperScannerConfigurer businessMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("wgn.myspringboot.bussiness.mapper");
        mapperScannerConfigurer.setSqlSessionTemplateBeanName("slaveSqlSessionTemplate");
        return mapperScannerConfigurer;
    }*/
}
