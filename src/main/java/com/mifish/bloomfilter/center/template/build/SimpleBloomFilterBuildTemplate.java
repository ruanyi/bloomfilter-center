package com.mifish.bloomfilter.center.template.build;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.repository.BloomFilterInputRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterLockRepository;
import com.mifish.bloomfilter.center.template.BloomFilterBuildTemplate;

import java.util.Date;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 21:23
 */
public class SimpleBloomFilterBuildTemplate extends AbstractBloomFilterBuildTemplate {

    /***bloomFilterInputRepository*/
    private BloomFilterInputRepository bloomFilterInputRepository;

//    /***bloomFilterTimeVersionRepository*/
//    private BloomFilterTimeVersionRepository bloomFilterTimeVersionRepository;

    /***bloomFilterLockRepository*/
    private BloomFilterLockRepository bloomFilterLockRepository;

    /**
     * getBloomFilterLockRepository
     *
     * @return
     */
    @Override
    public BloomFilterLockRepository getBloomFilterLockRepository() {
        return this.bloomFilterLockRepository;
    }

    public void setBloomFilterInputRepository(BloomFilterInputRepository bloomFilterInputRepository) {
        this.bloomFilterInputRepository = bloomFilterInputRepository;
    }

    public void setBloomFilterLockRepository(BloomFilterLockRepository bloomFilterLockRepository) {
        this.bloomFilterLockRepository = bloomFilterLockRepository;
    }
}
