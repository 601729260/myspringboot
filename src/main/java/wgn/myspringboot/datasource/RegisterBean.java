package wgn.myspringboot.datasource;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2018/10/12
 * Time           :   下午5:17
 * Description    :
 */
public class RegisterBean {


    public void Registry(ApplicationContext context) throws BeansException {
        try {
            IDataSourceProperties properties=(IDataSourceProperties) context.getBean("dataSourceProperties");
            List<DataSourceItem> dataSourceItems = properties.getDataSourceItems();

/*            DataSourceItem dataSourceItem1=new DataSourceItem();


            dataSourceItem1.setName("master");
            dataSourceItem1.setPassword("mamhao");
            dataSourceItem1.setUrl("jdbc:mysql://172.28.1.6:3306/db_gd_dev?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&rewriteBatchedStatements=true");
            dataSourceItem1.setUsername("root");
            dataSourceItem1.setBasePackage("wgn.myspringboot.primary.mapper");
            dataSourceItems.add(dataSourceItem1);


            DataSourceItem dataSourceItem2=new DataSourceItem();
            dataSourceItem2.setName("mamahaoPos");
            dataSourceItem2.setPassword("mamhao");
            dataSourceItem2.setUrl("jdbc:mysql://172.28.1.6:3306/mamahao_pos?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&rewriteBatchedStatements=true");
            dataSourceItem2.setUsername("root");
            dataSourceItem2.setBasePackage("wgn.myspringboot.bussiness.mapper");
            dataSourceItems.add(dataSourceItem2);*/

            for (DataSourceItem dataSourceItem : dataSourceItems) {
                if(dataSourceItem.getBasePackage()!=null) {

                    //初始化dataSource

                    DruidXADataSource xaDataSource = getAtomikosDataSourceBean(dataSourceItem);
                    String dataSourceName = dataSourceItem.getName() + "DataSource";

                    //初始化SqlSessionFactoryBean




                    DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)context.getAutowireCapableBeanFactory();
                    BeanDefinitionBuilder atomikosDataSourceBeanBuilder =BeanDefinitionBuilder.genericBeanDefinition(AtomikosDataSourceBean.class);
                    atomikosDataSourceBeanBuilder.addPropertyValue("xaDataSource",xaDataSource);
                    atomikosDataSourceBeanBuilder.addPropertyReference("uniqueResourceName",dataSourceName);


                    String  atomikosDataSourceBeanName=dataSourceItem.getName() + "AtomikosDataSource";
                    defaultListableBeanFactory.registerBeanDefinition(atomikosDataSourceBeanName,atomikosDataSourceBeanBuilder.getBeanDefinition());


                    //初始化SqlSessionFactoryBean

                    String sqlSessionFactoryBeanName=dataSourceItem.getName() + "SqlSessionFactoryBean";

                    BeanDefinitionBuilder SqlSessionFactoryBeanBuilder =BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class);
                    SqlSessionFactoryBeanBuilder.addPropertyReference("dataSource",atomikosDataSourceBeanName);
                    defaultListableBeanFactory.registerBeanDefinition(sqlSessionFactoryBeanName,SqlSessionFactoryBeanBuilder.getBeanDefinition());

                    //初始化SqlSessionFactory

                    String sqlSessionFactoryName=dataSourceItem.getName() + "SqlSessionFactory";
                    BeanDefinitionBuilder SqlSessionFactoryBuilder =BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactory.class);
                    SqlSessionFactoryBuilder.setFactoryMethodOnBean("getObject()",sqlSessionFactoryBeanName);
                    defaultListableBeanFactory.registerBeanDefinition(sqlSessionFactoryName,SqlSessionFactoryBuilder.getBeanDefinition());



                    //初始化SqlSessionTemplate

                    String sqlSessionTemplateName=dataSourceItem.getName() + "SqlSessionTemplate";


                    BeanDefinitionBuilder SqlSessionTemplateBuilder =BeanDefinitionBuilder.genericBeanDefinition(SqlSessionTemplate.class);
                    SqlSessionTemplateBuilder.addConstructorArgReference(sqlSessionFactoryName);
                    defaultListableBeanFactory.registerBeanDefinition(sqlSessionTemplateName,SqlSessionTemplateBuilder.getBeanDefinition());




                    //初始化mapperScannerConfigurer

                    String mapperScannerConfigurerName=dataSourceItem.getName() + "MapperScannerConfigurer";


                    BeanDefinitionBuilder mapperScannerConfigurerBuillder =BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
                    SqlSessionTemplateBuilder.addPropertyValue("basePackage",dataSourceItem.getBasePackage());
                    SqlSessionTemplateBuilder.addPropertyValue("sqlSessionTemplateBeanName",sqlSessionTemplateName);
                    defaultListableBeanFactory.registerBeanDefinition(mapperScannerConfigurerName,mapperScannerConfigurerBuillder.getBeanDefinition());

                }

            }
        } catch (BeansException e) {
            throw e;

        } catch (Exception e) {
            e.printStackTrace();
        }
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
