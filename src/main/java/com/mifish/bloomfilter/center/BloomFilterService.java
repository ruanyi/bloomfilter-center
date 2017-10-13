package com.mifish.bloomfilter.center;

/**
 * Description:
 * <p>
 * User: rls
 * Date: 2017-10-13 23:30
 */
public interface BloomFilterService {

    /**
     * isAvailable
     *
     * @param name
     * @return
     */
    boolean isAvailable(String name);

    /**
     * contains
     *
     * @param name
     * @param key
     * @return
     */
    boolean contains(String name, String key);
}
