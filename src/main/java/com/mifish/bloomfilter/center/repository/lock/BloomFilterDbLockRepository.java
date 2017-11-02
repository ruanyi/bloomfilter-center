package com.mifish.bloomfilter.center.repository.lock;

import com.mifish.bloomfilter.center.repository.BloomFilterLockRepository;

import java.util.Date;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 22:34
 */
public class BloomFilterDbLockRepository implements BloomFilterLockRepository {
    
    @Override
    public boolean obtainBloomFilterLock(String name, Date timeVersion, boolean isForced) {
        return false;
    }

    @Override
    public boolean releaseBloomFilterLock(String name, Date timeVersion) {
        return false;
    }
}
