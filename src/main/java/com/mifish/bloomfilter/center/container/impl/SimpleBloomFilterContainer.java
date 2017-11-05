package com.mifish.bloomfilter.center.container.impl;

import com.google.common.collect.Maps;
import com.mifish.bloomfilter.center.container.BloomFilterContainer;
import com.mifish.bloomfilter.center.model.UpdateStatus;
import com.mifish.bloomfilter.center.model.BloomFilterWrapper;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Description:
 * <p>
 * SimpleBloomFilterContainer
 *
 * @author: rls
 * Date: 2017-10-14 11:44
 */
public class SimpleBloomFilterContainer implements BloomFilterContainer {

    /***default bloomfilter name,just for log*/
    private String name = "DEFAULT_BLOOMFILTER_CONTAINER";

    /***bloomfilters*/
    private Map<String, BloomFilterWrapper> bloomfilters = Maps.newConcurrentMap();

    /**
     * getBloomFilterByName
     *
     * @param name
     * @return
     */
    @Override
    public BloomFilterWrapper getBloomFilterByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return this.bloomfilters.get(name);
    }

    /**
     * isBloomFilterExist
     *
     * @param name
     * @return
     */
    @Override
    public boolean isBloomFilterExist(String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        return this.bloomfilters.containsKey(name);
    }

    /**
     * getBloomFiterTimeVersion
     *
     * @param name
     * @return
     */
    @Override
    public Date getBloomFiterTimeVersion(String name) {
        BloomFilterWrapper bfwrapper = getBloomFilterByName(name);
        if (bfwrapper != null) {
            return bfwrapper.getTimeVersion();
        }
        return null;
    }

    /**
     * getAllBloomFilterNames
     *
     * @return
     */
    @Override
    public Set<String> getAllBloomFilterNames() {
        return new HashSet<>(this.bloomfilters.keySet());
    }

    /**
     * addBloomFilter
     *
     * @param name
     * @param bfwrapper
     * @return
     */
    @Override
    public BloomFilterWrapper addBloomFilter(String name, BloomFilterWrapper bfwrapper) {
        checkArgument(StringUtils.isNotBlank(name), "name cannot be blank when add a bloomfilter to " +
                "BloomFilterContainer");
        checkArgument(bfwrapper != null, "bf cannot be null when add a bloomfilter to BloomFilterContainer");
        BloomFilterWrapper oldBloomFilter = this.bloomfilters.put(name, bfwrapper);
        return oldBloomFilter;
    }

    /**
     * removeBloomfilter
     *
     * @param name
     * @return
     */
    @Override
    public BloomFilterWrapper removeBloomfilter(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return this.bloomfilters.remove(name);
    }

    /**
     * getName
     *
     * @return
     */
    @Override
    public String getName() {
        return this.name;
    }
}
