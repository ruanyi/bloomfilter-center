package com.mifish.bloomfilter.center.repository;

import java.util.Date;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 20:53
 */
public interface BloomFilterLockRepository {

    /**
     * obtainBloomFilterLock
     *
     * @param name
     * @param timeVersion
     * @param isForced
     * @return
     */
    boolean obtainBloomFilterLock(String name, Date timeVersion, boolean isForced);

    /**
     * releaseBloomFilterLock
     *
     * @param name
     * @param timeVersion
     * @return
     */
    boolean releaseBloomFilterLock(String name, Date timeVersion);
}
