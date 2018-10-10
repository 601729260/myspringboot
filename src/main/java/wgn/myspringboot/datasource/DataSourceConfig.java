package wgn.myspringboot.datasource;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2018/10/10
 * Time           :   下午5:21
 * Description    :
 */
public class DataSourceConfig {

    private static final String PRIMARY_MAPPER_BASE_PACKAGE = "io.github.yidasanqian.mapper.master";
    private static final String BUSINESS_MAPPER_BASE_PACKAGE = "io.github.yidasanqian.mapper.business";

    private static final String DATASOURCE_DRUID_PROPERTIES = "datasource/druid.properties";
    private static final String DATASOURCE_DRUID_PRIMARY_PROPERTIES = "datasource/druid-primary.properties";
    private static final String DATASOURCE_DRUID_BUSINESS_PROPERTIES = "datasource/druid-business.properties";

    private static final String CLASSPATH_MAPPER_XML = "classpath:mapper/*/*.xml";

    /**
     * druid 公共配置
     */
    @Bean
    @ConditionalOnMissingBean
    public IDataSourceProperties dataSourceProperties() {
        return new DefaultDatasourceProperties();
    }


    /**
     * 设置数据源
     *
     * @return
     * @throws IOException
     */
    @Primary
    @Bean
    public AtomikosDataSourceBean primaryDataSource(IDataSourceProperties properties) throws IOException {
        return getAtomikosDataSourceBean(properties.getDataSourceItems().get(0));
    }


    @Bean
    public AtomikosDataSourceBean businessDataSource(IDataSourceProperties properties) throws IOException {
        return getAtomikosDataSourceBean(properties.getDataSourceItems().get(1));
    }

    private AtomikosDataSourceBean getAtomikosDataSourceBean(DataSourceItem dataSourceItem) throws IOException {

        AtomikosDataSourceBean dataSourceBean = new AtomikosDataSourceBean();
        // 配置DruidXADataSource
        DruidXADataSource xaDataSource = new DruidXADataSource();

        Properties properties=new Properties();
        xaDataSource.configFromPropety(properties);
        // 设置置AtomikosDataSourceBean XADataSource
        dataSourceBean.setXaDataSource(xaDataSource);
        return dataSourceBean;
    }

    /**
     * 设置{@link SqlSessionFactoryBean}的数据源
     * @param primaryDataSource 主数据源
     * @return
     */
    @Primary
    @Bean
    public SqlSessionFactoryBean primarySqlSessionFactoryBean(@Qualifier("primaryDataSource") AtomikosDataSourceBean primaryDataSource) {
        return getSqlSessionFactoryBean(primaryDataSource);
    }

    @Bean
    public SqlSessionFactoryBean businessSqlSessionFactoryBean(@Qualifier("businessDataSource") AtomikosDataSourceBean businessDataSource) {
        return getSqlSessionFactoryBean(businessDataSource);
    }

    private SqlSessionFactoryBean getSqlSessionFactoryBean(AtomikosDataSourceBean dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources(CLASSPATH_MAPPER_XML));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sqlSessionFactoryBean;
    }

    /**
     * 搜索{@link DataSourceConfig#PRIMARY_MAPPER_BASE_PACKAGE} 包下的Mapper接口，并且将这些接口
     * 交由{@link MapperScannerConfigurer#sqlSessionFactoryBeanName} 属性设置的SqlSessionFactoryBean管理
     * @return
     */
    @Bean
    public MapperScannerConfigurer primaryMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage(PRIMARY_MAPPER_BASE_PACKAGE);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("primarySqlSessionFactoryBean");
        return mapperScannerConfigurer;
    }

    /**
     * 搜索{@link DataSourceConfig#BUSINESS_MAPPER_BASE_PACKAGE} 包下的Mapper接口，并且将这些接口
     * 交由{@link MapperScannerConfigurer#sqlSessionFactoryBeanName} 属性设置的SqlSessionFactoryBean管理
     * @return
     */
    @Bean
    public MapperScannerConfigurer businessMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage(BUSINESS_MAPPER_BASE_PACKAGE);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("businessSqlSessionFactoryBean");
        return mapperScannerConfigurer;
    }
}