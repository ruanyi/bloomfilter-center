package com.mifish.bloomfilter.center.trigger;

import com.mifish.bloomfilter.center.BloomFilterBuilder;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-14 11:39
 */
public interface BloomFilterBuildTrigger {

    /**
     * getBloomFilterBuilder
     *
     * @return
     */
    BloomFilterBuilder getBloomFilterBuilder();

    /**
     * triggerBuild
     *
     * @param isForced
     * @return
     */
    boolean triggerBuild(boolean isForced);
}
