package com.mifish.bloomfilter.center.trigger;

import com.mifish.bloomfilter.center.BloomFilterLoadService;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-14 12:23
 */
public abstract class AbstractBloomFilterLoadTrigger implements BloomFilterLoadTrigger {

    /***bloomFilterLoadService*/
    private BloomFilterLoadService bloomFilterLoadService;

    @Override
    public BloomFilterLoadService getBloomFilterLoadService() {
        return this.bloomFilterLoadService;
    }

    @Override
    public boolean triggerLoad(boolean isForced) {
        if (isForced) {
            return this.bloomFilterLoadService.foreLoad();
        }
        //normal loader
        return this.bloomFilterLoadService.load();
    }

    /**
     * getBloomFilterName
     *
     * @return
     */
    protected String getBloomFilterName() {
        return getBloomFilterLoadService().getBloomFilterName();
    }

    /**
     * getBloomFilterGroup
     *
     * @return
     */
    protected String getBloomFilterGroup() {
        return getBloomFilterLoadService().group();
    }

    /**
     * getBloomFilterOrder
     *
     * @return
     */
    protected int getBloomFilterOrder() {
        return getBloomFilterLoadService().order();
    }

    /**
     * setBloomFilterLoadService
     *
     * @param bloomFilterLoadService
     */
    public void setBloomFilterLoadService(BloomFilterLoadService bloomFilterLoadService) {
        this.bloomFilterLoadService = bloomFilterLoadService;
    }
}
