package com.mifish.bloomfilter.center.repository;

import com.mifish.bloomfilter.center.model.BloomFilterWrapper;

import java.util.Collection;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 20:39
 */
public interface BloomFilterOutputRepository {

    /**
     * storeBloomFilter
     *
     * @param bfwrapper
     * @return
     */
    boolean storeBloomFilter(BloomFilterWrapper bfwrapper);

    /**
     * batchStoreBloomFilters
     * <p>
     * consistence
     *
     * @param bfs
     * @return
     */
    boolean batchStoreBloomFilters(Collection<BloomFilterWrapper> bfs);
}
