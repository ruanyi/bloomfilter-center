package com.mifish.bloomfilter.center.repository.lock;

import com.google.common.collect.Maps;
import com.mifish.bloomfilter.center.container.BloomFilterContainer;
import com.mifish.bloomfilter.center.model.UpdateStatus;
import com.mifish.bloomfilter.center.repository.BloomFilterLockRepository;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 22:29
 */
public class BloomFilterContainerLockRepository implements BloomFilterLockRepository {

    /***bloomFilterContainer*/
    private BloomFilterContainer bloomFilterContainer;

    /***bfStatus*/
    private Map<String, UpdateStatus> bfStatus = Maps.newConcurrentMap();

    /***lock*/
    private final Lock lock = new ReentrantLock();

    /**
     * obtainBloomFilterLock
     *
     * @param name
     * @param timeVersion
     * @param isForced
     * @return
     */
    @Override
    public boolean obtainBloomFilterLock(String name, Date timeVersion, boolean isForced) {
        if (StringUtils.isBlank(name) || timeVersion == null) {
            return false;
        }
        if (getUpdateStatus(name) != UpdateStatus.UPDATING) {
            try {
                lock.lock();
                if (getUpdateStatus(name) != UpdateStatus.UPDATING) {
                    Date tvInC = this.bloomFilterContainer.getBloomFiterTimeVersion(name);
                    if (tvInC == null || isForced || timeVersion.getTime() > tvInC.getTime()) {
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
     * releaseBloomFilterLock
     *
     * @param name
     * @param timeVersion
     * @return
     */
    @Override
    public boolean releaseBloomFilterLock(String name, Date timeVersion) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        if (this.getUpdateStatus(name) == UpdateStatus.UPDATING) {
            try {
                lock.lock();
                if (this.getUpdateStatus(name) == UpdateStatus.UPDATING) {
                    this.bfStatus.put(name, UpdateStatus.UPDATE_SUCCESS);
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
    private UpdateStatus getUpdateStatus(String name) {
        if (this.bfStatus.containsKey(name)) {
            return this.bfStatus.get(name);
        }
        return UpdateStatus.INIT;
    }

    public void setBloomFilterContainer(BloomFilterContainer bloomFilterContainer) {
        this.bloomFilterContainer = bloomFilterContainer;
    }
}


