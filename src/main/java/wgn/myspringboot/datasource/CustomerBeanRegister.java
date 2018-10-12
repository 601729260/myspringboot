
package wgn.myspringboot.datasource;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.serviceloader.ServiceFactoryBean;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.mapper.weekend.reflection.Reflections;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2018/10/12
 * Time           :   上午9:53
 * Description    :
 */

@Configuration
public class CustomerBeanRegister implements BeanDefinitionRegistryPostProcessor {


    @Bean
    @ConditionalOnMissingBean
    public IDataSourceProperties dataSourceProperties() {
        return new DefaultDatasourceProperties();
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        try {

            IDataSourceProperties properties=dataSourceProperties();
            List<DataSourceItem> dataSourceItems = properties.getDataSourceItems();

            DataSourceItem dataSourceItem1=new DataSourceItem();


            dataSourceItem1.setName("master");
            dataSourceItem1.setPassword("mamahao");
            dataSourceItem1.setUrl("jdbc:mysql://172.28.1.6:3306/db_gd_dev?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&rewriteBatchedStatements=true");
            dataSourceItem1.setUsername("root");
            dataSourceItem1.setBasePackage("wgn.myspringboot.primary.mapper");
            dataSourceItems.add(dataSourceItem1);


            DataSourceItem dataSourceItem2=new DataSourceItem();
            dataSourceItem2.setName("mamahaoPos");
            dataSourceItem2.setPassword("mamahao");
            dataSourceItem2.setUrl("jdbc:mysql://172.28.1.6:3306/mamahao_pos?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&rewriteBatchedStatements=true");
            dataSourceItem2.setUsername("root");
            dataSourceItem2.setBasePackage("wgn.myspringboot.bussiness.mapper");
            dataSourceItems.add(dataSourceItem2);

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
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

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


}


