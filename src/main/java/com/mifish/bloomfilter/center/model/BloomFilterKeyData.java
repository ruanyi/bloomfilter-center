package com.mifish.bloomfilter.center.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 20:46
 */
public class BloomFilterKeyData implements Serializable {

    private static final long serialVersionUID = 4493934391902742517L;

    /***key*/
    private String key;

    /***attributes*/
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * addAttribute
     *
     * @param key
     * @param value
     */
    public void addAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    /**
     * addAttributes
     *
     * @param attributes
     */
    public void addAttributes(Map<String, Object> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return;
        }
        this.attributes.putAll(attributes);
    }

    /**
     * getAttribute
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getAttribute(String key, Class<T> clazz) {
        Object obj = this.attributes.get(key);
        if (obj == null) {
            return null;
        }
        return clazz.cast(obj);
    }
}
