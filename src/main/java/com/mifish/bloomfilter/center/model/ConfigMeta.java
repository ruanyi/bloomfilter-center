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

    /**
     * getName
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * setName
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getTimeVersion
     *
     * @return
     */
    public Date getTimeVersion() {
        return timeVersion;
    }

    /**
     * setTimeVersion
     *
     * @param timeVersion
     */
    public void setTimeVersion(Date timeVersion) {
        this.timeVersion = timeVersion;
    }

    /**
     * getUrl
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * setUrl
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigMeta that = (ConfigMeta) o;
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (timeVersion != null ? !timeVersion.equals(that.timeVersion) : that.timeVersion != null) {
            return false;
        }
        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (timeVersion != null ? timeVersion.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConfigMeta{" +
                "name='" + name + '\'' +
                ", timeVersion=" + timeVersion +
                ", url='" + url + '\'' +
                '}';
    }
}
