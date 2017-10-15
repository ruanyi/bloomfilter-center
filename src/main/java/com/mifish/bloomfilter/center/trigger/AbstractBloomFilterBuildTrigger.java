package com.mifish.bloomfilter.center.trigger;

import com.mifish.bloomfilter.center.BloomFilterBuildService;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 14:01
 */
public class AbstractBloomFilterBuildTrigger implements BloomFilterBuildTrigger {

    /***bloomFilterBuilder*/
    private BloomFilterBuildService bloomFilterBuilder;

    /**
     * getBloomFilterBuilder
     *
     * @return
     */
    @Override
    public BloomFilterBuildService getBloomFilterBuilder() {
        return this.bloomFilterBuilder;
    }

    /**
     * getBloomFilterName
     *
     * @return
     */
    protected String getBloomFilterName() {
        return getBloomFilterBuilder().getBloomFilterName();
    }

    /**
     * getBloomFilterGroup
     *
     * @return
     */
    protected String getBloomFilterGroup() {
        return getBloomFilterBuilder().group();
    }

    /**
     * getBloomFilterOrder
     *
     * @return
     */
    protected int getBloomFilterOrder() {
        return getBloomFilterBuilder().order();
    }

    /**
     * triggerBuild
     *
     * @param isForced
     * @return
     */
    @Override
    public boolean triggerBuild(boolean isForced) {
        if (isForced) {
            return this.bloomFilterBuilder.foreBuild();
        }
        return this.bloomFilterBuilder.build();
    }

    public void setBloomFilterBuilder(BloomFilterBuildService bloomFilterBuilder) {
        this.bloomFilterBuilder = bloomFilterBuilder;
    }
}
