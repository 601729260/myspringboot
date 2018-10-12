package wgn.myspringboot.datasource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2018/10/12
 * Time           :   上午11:46
 * Description    :
 */
@Configuration
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    private RegisterBean registerBean=new RegisterBean();

    public ApplicationContextProvider(){}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
       // registerBean.Registry(context);
    }

    public static <T> T getBean(String name,Class<T> aClass){
        return context.getBean(name,aClass);
    }

    public static <T> T getBean(Class<T> aClass){
        return context.getBean(aClass);
    }


}