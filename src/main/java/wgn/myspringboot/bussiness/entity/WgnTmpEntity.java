package wgn.myspringboot.bussiness.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.ibatis.type.Alias;

/**
 * 
 */

@Entity
@Table(name="wgn_tmp")
@Alias("WgnTmpEntity")
public class WgnTmpEntity {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String name;

    /**
     * 
     * @return id 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return name 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}