package com.mifish.bloomfilter.center.container.impl;

import com.google.common.collect.Maps;
import com.mifish.bloomfilter.center.container.BloomFilterContainer;
import com.mifish.bloomfilter.center.model.BloomFilterWrapper;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 * <p>
 * SimpleBloomFilterContainer
 *
 * @author: rls
 * Date: 2017-10-14 11:44
 */
public class SimpleBloomFilterContainer implements BloomFilterContainer {


    /**
     * default bloomfilter name,just for log
     */
    private String name = "DEFAULT_BLOOMFILTER_CONTAINER";

    /**
     * bloomfilters
     */
    private Map<String, BloomFilterWrapper> bloomfilters = Maps.newConcurrentMap();


    @Override
    public BloomFilterWrapper getBloomFilterByName(String name) {
        if (name == null || "".equalsIgnoreCase(name)) {
            return null;
        }
        return bloomfilters.get(name);
    }

    @Override
    public boolean isBloomFilterExist(String name) {
        if (name == null || "".equalsIgnoreCase(name)) {
            return false;
        }
        return this.bloomfilters.containsKey(name);
    }

    @Override
    public Date getBloomFiterTimeVersion(String name) {
        return null;
    }

    @Override
    public Set<String> getAllBloomFilterNames() {
        return null;
    }

    @Override
    public boolean addBloomFilter(BloomFilterWrapper bfwrapper, String name) {
        return false;
    }

    @Override
    public BloomFilterWrapper removeBloomfilter(String name) {
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
