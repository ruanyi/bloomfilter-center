package com.mifish.bloomfilter.center.repository;

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
    byte[] queryBloomFilterByName(String name);
}
