package com.mifish.bloomfilter.center.container;

import com.mifish.bloomfilter.center.model.BloomFilterWrapper;
import com.mifish.bloomfilter.center.model.UpdateStatus;

import java.util.Date;
import java.util.Set;

/**
 * Description:
 * <p>
 *
 * @author : rls
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
     * @param name
     * @param bfwrapper
     * @return
     */
    boolean addBloomFilter(String name, BloomFilterWrapper bfwrapper);

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


