package com.mifish.bloomfilter.center.template;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.repository.BloomFilterLockRepository;

import java.util.Date;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-14 11:53
 */
public interface BloomFilterBuildTemplate {

    /**
     * build
     *
     * @param bloomFilterTask
     * @param startTaskTime
     * @return
     */
    BloomFilterTaskResult build(BloomFilterTask bloomFilterTask, Date startTaskTime);

    /**
     * getBloomFilterLockRepository
     *
     * @return
     */
    BloomFilterLockRepository getBloomFilterLockRepository();

}
