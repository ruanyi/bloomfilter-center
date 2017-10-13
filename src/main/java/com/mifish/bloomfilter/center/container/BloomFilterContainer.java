package com.mifish.bloomfilter.center.container;

import com.mifish.bloomfilter.center.model.BloomFilterWrapper;

import java.util.Date;
import java.util.Set;

/**
 * Description:
 * <p>
 * User: rls
 * Date: 2017-10-13 21:04
 */
public interface BloomFilterContainer {

    /**
     * getBloomFilterByName
     *
     * @param name
     * @return
     */
    BloomFilterWrapper getBloomFilterByName(String name);

    /**
     * isBloomFilterExist
     *
     * @param name
     * @return
     */
    boolean isBloomFilterExist(String name);

    /**
     * getBloomFiterTimeVersion
     *
     * @param name
     * @return
     */
    Date getBloomFiterTimeVersion(String name);

    /**
     * getAllBloomFilterNames
     *
     * @return
     */
    Set<String> getAllBloomFilterNames();

    /**
     * addBloomFilter
     *
     * @param bfwrapper
     * @param name
     * @return
     */
    boolean addBloomFilter(BloomFilterWrapper bfwrapper, String name);

    /**
     * removeBloomfilter
     *
     * @param name
     * @return
     */
    BloomFilterWrapper removeBloomfilter(String name);

    /**
     * obtain bloomfilter container name
     *
     * @return
     */
    String getName();

}
