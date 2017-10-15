package com.mifish.bloomfilter.center.trigger;

import com.mifish.bloomfilter.center.BloomFilterLoadService;

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
    BloomFilterLoadService getBloomFilterLoadService();

    /**
     * triggerLoad
     *
     * @param isForced
     * @return
     */
    boolean triggerLoad(boolean isForced);
}
