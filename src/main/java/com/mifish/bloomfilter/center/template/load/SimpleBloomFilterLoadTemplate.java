package com.mifish.bloomfilter.center.template.load;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.repository.BloomFilterConfigRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterInputRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterLockRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterOutputRepository;
import com.mifish.bloomfilter.center.template.BloomFilterLoadTemplate;

import java.util.Date;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 21:12
 */
public class SimpleBloomFilterLoadTemplate implements BloomFilterLoadTemplate {

    /***bloomFilterInputRepository*/
    private BloomFilterInputRepository bloomFilterInputRepository;

    /***bloomFilterLockRepository*/
    private BloomFilterLockRepository bloomFilterLockRepository;

    /***bloomFilterConfigRepository*/
    private BloomFilterConfigRepository bloomFilterConfigRepository;

    /***bloomFilterOutputRepository*/
    private BloomFilterOutputRepository bloomFilterOutputRepository;

    /**
     * load
     *
     * @param task
     * @param startTaskTime
     * @return
     */
    @Override
    public BloomFilterTaskResult load(BloomFilterTask task, Date startTaskTime) {
        return null;
    }

    /**
     * getBloomFilterLockRepository
     *
     * @return
     */
    @Override
    public BloomFilterLockRepository getBloomFilterLockRepository() {
        return this.bloomFilterLockRepository;
    }

    /**
     * getBloomFilterConfigRepository
     *
     * @return
     */
    @Override
    public BloomFilterConfigRepository getBloomFilterConfigRepository() {
        return this.bloomFilterConfigRepository;
    }

    @Override
    public BloomFilterOutputRepository getBloomFilterOutputRepository() {
        return this.bloomFilterOutputRepository;
    }

    public void setBloomFilterInputRepository(BloomFilterInputRepository bloomFilterInputRepository) {
        this.bloomFilterInputRepository = bloomFilterInputRepository;
    }

    public void setBloomFilterLockRepository(BloomFilterLockRepository bloomFilterLockRepository) {
        this.bloomFilterLockRepository = bloomFilterLockRepository;
    }

    public void setBloomFilterConfigRepository(BloomFilterConfigRepository bloomFilterConfigRepository) {
        this.bloomFilterConfigRepository = bloomFilterConfigRepository;
    }
}
