package com.mifish.bloomfilter.center.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 23:36
 */
public class ConfigMeta implements Serializable {

    private static final long serialVersionUID = 2012290827163053560L;

    /***name*/
    private String name;

    /***timeVersion*/
    private Date timeVersion;

    /***url*/
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTimeVersion() {
        return timeVersion;
    }

    public void setTimeVersion(Date timeVersion) {
        this.timeVersion = timeVersion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
