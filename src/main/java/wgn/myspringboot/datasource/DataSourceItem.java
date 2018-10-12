package wgn.myspringboot.datasource;

import java.util.Objects;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2018/10/10
 * Time           :   下午5:25
 * Description    :
 */
public class DataSourceItem {
    private String name;
    private boolean master = false;
    private String url;
    private String username;
    private String password;
    private String basePackage;
    private String mapperLocations;

    public DataSourceItem() {
    }

    public DataSourceItem(String name, boolean master, String url, String username, String password) {
        this.name = name;
        this.master = master;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMaster() {
        return this.master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }


}
