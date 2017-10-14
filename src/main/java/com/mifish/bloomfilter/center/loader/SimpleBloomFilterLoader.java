package com.mifish.bloomfilter.center.loader;

import com.mifish.bloomfilter.center.BloomFilterLoader;
import com.mifish.bloomfilter.center.worker.BloomFilterTaskWorker;

/**
 * Description:
 * <p>
 *
 * @author: rls
 * Date: 2017-10-14 11:29
 */
public class SimpleBloomFilterLoader implements BloomFilterLoader {

    /**
     * order
     */
    private int order = 0;

    /**
     * bloomFilterName
     */
    private String bloomFilterName;


    /**
     * bloomFilterTaskWorker
     */
    private BloomFilterTaskWorker bloomFilterTaskWorker;

    /**
     * SimpleBloomFilterLoader
     *
     * @param order
     * @param bloomFilterName
     */
    public SimpleBloomFilterLoader(int order, String bloomFilterName) {
        this.order = order;
        this.bloomFilterName = bloomFilterName;
    }

    @Override
    public int order() {
        return this.order;
    }

    @Override
    public String getBloomFilterName() {
        return this.bloomFilterName;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setBloomFilterName(String bloomFilterName) {
        this.bloomFilterName = bloomFilterName;
    }

    @Override
    public boolean load() {
        return false;
    }

    @Override
    public boolean foreLoad() {
        return false;
    }

    public void setBloomFilterTaskWorker(BloomFilterTaskWorker bloomFilterTaskWorker) {
        this.bloomFilterTaskWorker = bloomFilterTaskWorker;
    }
}
