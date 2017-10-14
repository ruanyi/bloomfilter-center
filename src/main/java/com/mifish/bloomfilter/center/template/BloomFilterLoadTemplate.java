package com.mifish.bloomfilter.center.template;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.model.BloomFilterWrapper;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-14 11:50
 */
public interface BloomFilterLoadTemplate {

    /**
     * load
     *
     * @param task
     * @return
     */
    BloomFilterTaskResult load(BloomFilterTask task);
}
