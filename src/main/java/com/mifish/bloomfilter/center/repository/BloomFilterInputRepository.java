package com.mifish.bloomfilter.center.repository;

import com.mifish.bloomfilter.center.model.BloomFilterWrapper;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 20:38
 */
public interface BloomFilterInputRepository {

    /**
     * queryBloomFilterByName
     *
     * @param name
     * @return
     */
    BloomFilterWrapper queryBloomFilterByName(String name);
}
