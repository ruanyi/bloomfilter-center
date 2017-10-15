package com.mifish.bloomfilter.center.container.impl;

import com.google.common.collect.Maps;
import com.mifish.bloomfilter.center.container.BloomFilterContainer;
import com.mifish.bloomfilter.center.container.UpdateStatus;
import com.mifish.bloomfilter.center.model.BloomFilterWrapper;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.apache.commons.lang.StringUtils;

import javax.print.attribute.standard.MediaSize;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    /***bfStatus*/
    private Map<String, UpdateStatus> bfStatus = Maps.newConcurrentMap();

    /***lock*/
    private final Lock lock = new ReentrantLock();


    @Override
    public BloomFilterWrapper getBloomFilterByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return bloomfilters.get(name);
    }

    @Override
    public boolean isBloomFilterExist(String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        return this.bloomfilters.containsKey(name);
    }

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

    @Override
    public boolean addBloomFilter(String name, BloomFilterWrapper bfwrapper) {
        if (isNeedAddBloomFilter(name, bfwrapper)) {
            try {
                lock.lock();
                if (isNeedAddBloomFilter(name, bfwrapper)) {
                    bloomfilters.put(name, bfwrapper);
                    return true;
                }
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    /**
     * isNeedAddBloomFilter
     *
     * @param name
     * @param bfwrapper
     * @return
     */
    private boolean isNeedAddBloomFilter(String name, BloomFilterWrapper bfwrapper) {
        if (StringUtils.isBlank(name) || bfwrapper == null || bfwrapper.getTimeVersion() == null) {
            return false;
        }
        if (getUpdateStatus(name) != UpdateStatus.UPDATING) {
            return false;
        }
        if (!isBloomFilterExist(name)) {
            return true;
        }
        Date timeVersion = getBloomFiterTimeVersion(name);
        if (timeVersion.getTime() <= bfwrapper.getTimeVersion().getTime()) {
            return true;
        }
        return false;
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
     * obtainUpdatePermission
     *
     * @param name
     * @param isForced
     * @param newVersion
     * @return
     */
    @Override
    public boolean obtainUpdatePermission(String name, boolean isForced, Date newVersion) {
        if (StringUtils.isBlank(name) || newVersion == null) {
            return false;
        }
        if (getUpdateStatus(name) != UpdateStatus.UPDATING) {
            try {
                lock.lock();
                if (getUpdateStatus(name) != UpdateStatus.UPDATING) {
                    Date tvInC = this.getBloomFiterTimeVersion(name);
                    if (tvInC == null || isForced || newVersion.getTime() > tvInC.getTime()) {
                        this.bfStatus.put(name, UpdateStatus.UPDATING);
                        return true;
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    /**
     * releaseUpdatePermission
     *
     * @param name
     * @param status
     * @return
     */
    @Override
    public boolean releaseUpdatePermission(String name, UpdateStatus status) {
        if (StringUtils.isBlank(name) || status == null || status != UpdateStatus.UPDATING) {
            return false;
        }
        if (this.getUpdateStatus(name) == UpdateStatus.UPDATING) {
            try {
                lock.lock();
                if (this.getUpdateStatus(name) == UpdateStatus.UPDATING) {
                    this.bfStatus.put(name, status);
                    return true;
                }
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    /**
     * getUpdateStatus
     *
     * @param name
     * @return
     */
    @Override
    public UpdateStatus getUpdateStatus(String name) {
        if (this.bfStatus.containsKey(name)) {
            return this.bfStatus.get(name);
        }
        return UpdateStatus.INIT;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
