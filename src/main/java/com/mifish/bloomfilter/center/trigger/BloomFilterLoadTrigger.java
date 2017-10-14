package com.mifish.bloomfilter.center.trigger;

import com.mifish.bloomfilter.center.BloomFilterLoader;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-14 11:37
 */
public interface BloomFilterLoadTrigger {

    /**
     * getBloomFilterLoader
     *
     * @return
     */
    BloomFilterLoader getBloomFilterLoader();

    /**
     * triggerLoad
     *
     * @param isForced
     * @return
     */
    boolean triggerLoad(boolean isForced);
}
