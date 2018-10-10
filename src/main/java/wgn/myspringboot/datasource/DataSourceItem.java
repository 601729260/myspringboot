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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSourceItem that = (DataSourceItem) o;
        return isMaster() == that.isMaster() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getUrl(), that.getUrl()) &&
                Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), isMaster(), getUrl(), getUsername(), getPassword());
    }
}
