package wgn.myspringboot.datasource;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.spring.annotation.MapperScan;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;
import wgn.myspringboot.mybatis.IMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    @Bean(name="dataSourceProperties")
    @ConditionalOnMissingBean
    public IDataSourceProperties dataSourceProperties() {
        return new DefaultDatasourceProperties();
    }

    @Bean
    public BeanDefinitionRegistryPostProcessor beanDefinitionRegistryPostProcessor(Environment env) {
        return new BeanDefinitionRegistryPostProcessor() {
            @Override
            public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
                try {
                    List<DataSourceItem> dataSourceItems=new ArrayList<>();
                    Integer i=0;
                    String key="wgn.data.dataSource.dataSourceItems["+i+"].";
                    while(env.getProperty(key+"url")!=null){

                        DataSourceItem dataSourceItem1=new DataSourceItem();


                        dataSourceItem1.setName(env.getProperty(key+"name"));
                        dataSourceItem1.setPassword(env.getProperty(key+"password"));
                        dataSourceItem1.setUrl(env.getProperty(key+"url"));
                        dataSourceItem1.setUsername(env.getProperty(key+"username"));
                        dataSourceItem1.setBasePackage(env.getProperty(key+"basePackage"));
                        dataSourceItems.add(dataSourceItem1);


                        i++;
                        key="wgn.data.dataSource.dataSourceItems["+i+"].";
                    }

                    for (DataSourceItem dataSourceItem : dataSourceItems) {
                        if(dataSourceItem.getBasePackage()!=null) {

                            //初始化dataSource

                            DruidXADataSource xaDataSource = getAtomikosDataSourceBean(dataSourceItem);
                            String dataSourceName = dataSourceItem.getName() + "DataSource";

                            //初始化SqlSessionFactoryBean




                            BeanDefinitionBuilder atomikosDataSourceBeanBuilder =BeanDefinitionBuilder.genericBeanDefinition(AtomikosDataSourceBean.class);
                            atomikosDataSourceBeanBuilder.addPropertyValue("xaDataSource",xaDataSource);
                            atomikosDataSourceBeanBuilder.addPropertyValue("uniqueResourceName",dataSourceName);


                            String  atomikosDataSourceBeanName=dataSourceItem.getName() + "AtomikosDataSource";
                            registry.registerBeanDefinition(atomikosDataSourceBeanName,atomikosDataSourceBeanBuilder.getBeanDefinition());


                            //初始化SqlSessionFactoryBean

                            String sqlSessionFactoryBeanName=dataSourceItem.getName() + "SqlSessionFactoryBean";

                            BeanDefinitionBuilder SqlSessionFactoryBeanBuilder =BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class);
                            SqlSessionFactoryBeanBuilder.addPropertyReference("dataSource",atomikosDataSourceBeanName);
                            registry.registerBeanDefinition(sqlSessionFactoryBeanName,SqlSessionFactoryBeanBuilder.getBeanDefinition());


                  /*  //初始化SqlSessionFactory

                    String sqlSessionFactoryName=dataSourceItem.getName() + "SqlSessionFactory";
                    BeanDefinitionBuilder SqlSessionFactoryBuilder =BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactory.class);
                    SqlSessionFactoryBuilder.addDependsOn(sqlSessionFactoryBeanName);
                    registry.registerBeanDefinition(sqlSessionFactoryName,SqlSessionFactoryBuilder.getBeanDefinition());




                    //初始化SqlSessionTemplate

                    String sqlSessionTemplateName=dataSourceItem.getName() + "SqlSessionTemplate";


                    BeanDefinitionBuilder SqlSessionTemplateBuilder =BeanDefinitionBuilder.genericBeanDefinition(SqlSessionTemplate.class);
                    SqlSessionTemplateBuilder.addConstructorArgReference(sqlSessionFactoryName);
                    registry.registerBeanDefinition(sqlSessionTemplateName,SqlSessionTemplateBuilder.getBeanDefinition());

*/


                            //初始化mapperScannerConfigurer

                            String mapperScannerConfigurerName=dataSourceItem.getName() + "MapperScannerConfigurer";


                            BeanDefinitionBuilder mapperScannerConfigurerBuillder =BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
                            mapperScannerConfigurerBuillder.addPropertyValue("basePackage",dataSourceItem.getBasePackage());
                            mapperScannerConfigurerBuillder.addPropertyValue("sqlSessionFactoryBeanName",sqlSessionFactoryBeanName);
                            registry.registerBeanDefinition(mapperScannerConfigurerName,mapperScannerConfigurerBuillder.getBeanDefinition());

                        }

                    }
                } catch (BeansException e) {
                    throw e;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

            }



            private DruidXADataSource getAtomikosDataSourceBean(DataSourceItem dataSourceItem) throws IOException {


                // 配置DruidXADataSource
                DruidXADataSource xaDataSource = new DruidXADataSource();

                Properties properties = new Properties();

                properties.setProperty("druid.name", dataSourceItem.getName());
                properties.setProperty("druid.url", dataSourceItem.getUrl());
                properties.setProperty("druid.username", dataSourceItem.getUsername());
                properties.setProperty("druid.password", dataSourceItem.getPassword());
                xaDataSource.configFromPropety(properties);
                // 设置置AtomikosDataSourceBean XADataSource

                return xaDataSource;
            }
        };

    }

}