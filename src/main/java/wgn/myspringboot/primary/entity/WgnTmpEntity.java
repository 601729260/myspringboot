package wgn.myspringboot.primary.entity;

import org.apache.ibatis.type.Alias;

import javax.persistence.Entity;
import javax.persistence.Table;

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