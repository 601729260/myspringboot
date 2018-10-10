package wgn.myspringboot.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2018/10/10
 * Time           :   下午5:26
 * Description    :
 */
@ConfigurationProperties(
        prefix = "wgn.data.dataSource",
        ignoreInvalidFields = true,
        exceptionIfInvalid = false
)
public class DefaultDatasourceProperties implements IDataSourceProperties {
    private String defaultDataSourceType;
    private String defaultDriverClassName;
    private List<DataSourceItem> dataSourceItems = new ArrayList(2);

    public DefaultDatasourceProperties() {
    }

    public String getDefaultDataSourceType() {
        return this.defaultDataSourceType;
    }

    public void setDefaultDataSourceType(String defaultDataSourceType) {
        this.defaultDataSourceType = defaultDataSourceType;
    }

    public String getDefaultDriverClassName() {
        return this.defaultDriverClassName;
    }

    public void setDefaultDriverClassName(String defaultDriverClassName) {
        this.defaultDriverClassName = defaultDriverClassName;
    }

    public List<DataSourceItem> getDataSourceItems() {
        return this.dataSourceItems;
    }

    public void setDataSourceItems(List<DataSourceItem> dataSourceItems) {
        this.dataSourceItems = dataSourceItems;
    }
}
