package com.mifish.bloomfilter.center.trigger;

import com.mifish.bloomfilter.center.BloomFilterLoadService;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-14 12:23
 */
public abstract class AbstractBloomFilterLoadTrigger implements BloomFilterLoadTrigger {

    /***bloomFilterLoader*/
    private BloomFilterLoadService bloomFilterLoader;

    @Override
    public BloomFilterLoadService getBloomFilterLoader() {
        return this.bloomFilterLoader;
    }

    @Override
    public boolean triggerLoad(boolean isForced) {
        if (isForced) {
            return this.bloomFilterLoader.foreLoad();
        }
        //normal loader
        return this.bloomFilterLoader.load();
    }

    /**
     * getBloomFilterName
     *
     * @return
     */
    protected String getBloomFilterName() {
        return getBloomFilterLoader().getBloomFilterName();
    }

    /**
     * getBloomFilterGroup
     *
     * @return
     */
    protected String getBloomFilterGroup() {
        return getBloomFilterLoader().group();
    }

    /**
     * getBloomFilterOrder
     *
     * @return
     */
    protected int getBloomFilterOrder() {
        return getBloomFilterLoader().order();
    }

    /**
     * setBloomFilterLoader
     *
     * @param bloomFilterLoader
     */
    public void setBloomFilterLoader(BloomFilterLoadService bloomFilterLoader) {
        this.bloomFilterLoader = bloomFilterLoader;
    }
}
