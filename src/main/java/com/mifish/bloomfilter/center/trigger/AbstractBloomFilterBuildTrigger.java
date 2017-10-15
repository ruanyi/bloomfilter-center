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
    private BloomFilterBuildService bloomFilterBuildService;

    /**
     * getBloomFilterBuildService
     *
     * @return
     */
    @Override
    public BloomFilterBuildService getBloomFilterBuildService() {
        return this.bloomFilterBuildService;
    }

    /**
     * getBloomFilterName
     *
     * @return
     */
    protected String getBloomFilterName() {
        return getBloomFilterBuildService().getBloomFilterName();
    }

    /**
     * getBloomFilterGroup
     *
     * @return
     */
    protected String getBloomFilterGroup() {
        return getBloomFilterBuildService().group();
    }

    /**
     * getBloomFilterOrder
     *
     * @return
     */
    protected int getBloomFilterOrder() {
        return getBloomFilterBuildService().order();
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
            return this.bloomFilterBuildService.foreBuild();
        }
        return this.bloomFilterBuildService.build();
    }

    /**
     * setBloomFilterBuildService
     *
     * @param bloomFilterBuildService
     */
    public void setBloomFilterBuildService(BloomFilterBuildService bloomFilterBuildService) {
        this.bloomFilterBuildService = bloomFilterBuildService;
    }
}
