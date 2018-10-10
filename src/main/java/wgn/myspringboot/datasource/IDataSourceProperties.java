package wgn.myspringboot.datasource;

import java.util.List;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2018/10/10
 * Time           :   下午5:23
 * Description    :
 */
public interface IDataSourceProperties {
    String getDefaultDataSourceType();

    String getDefaultDriverClassName();

    List<DataSourceItem> getDataSourceItems();
}

