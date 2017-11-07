package com.mifish.bloomfilter.center.template.load;

import com.mifish.bloomfilter.center.repository.BloomFilterConfigRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterInputRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterLockRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterOutputRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 21:12
 */
public class SimpleBloomFilterLoadTemplate extends AbstractBloomFilterLoadTemplate {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(SimpleBloomFilterLoadTemplate.class);

    /***bloomFilterInputRepository*/
    private BloomFilterInputRepository bloomFilterInputRepository;

    /***bloomFilterLockRepository*/
    private BloomFilterLockRepository bloomFilterLockRepository;

    /***bloomFilterConfigRepository*/
    private BloomFilterConfigRepository bloomFilterConfigRepository;

    /***bloomFilterOutputRepository*/
    private BloomFilterOutputRepository bloomFilterOutputRepository;

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

    @Override
    public BloomFilterInputRepository getBloomFilterInputRepository() {
        return this.bloomFilterInputRepository;
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
