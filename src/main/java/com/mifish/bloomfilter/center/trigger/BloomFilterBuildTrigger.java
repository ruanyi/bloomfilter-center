package com.mifish.bloomfilter.center.trigger;

import com.mifish.bloomfilter.center.BloomFilterBuildService;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-14 11:39
 */
public interface BloomFilterBuildTrigger {

    /**
     * getBloomFilterBuildService
     *
     * @return
     */
    BloomFilterBuildService getBloomFilterBuildService();

    /**
     * triggerBuild
     *
     * @param isForced
     * @return
     */
    boolean triggerBuild(boolean isForced);
}
