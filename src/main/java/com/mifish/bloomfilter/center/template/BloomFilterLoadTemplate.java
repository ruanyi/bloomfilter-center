package com.mifish.bloomfilter.center.template;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-14 11:50
 */
public interface BloomFilterLoadTemplate {

    /**
     * loader
     *
     * @param task
     * @return
     */
    BloomFilterTaskResult load(BloomFilterTask task);
}
