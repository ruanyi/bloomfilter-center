package com.mifish.bloomfilter.center.template.load;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.repository.BloomFilterInputRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterTimeVersionRepository;
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

    /***bloomFilterTimeVersionRepository*/
    private BloomFilterTimeVersionRepository bloomFilterTimeVersionRepository;

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
}
