package com.mifish.bloomfilter.center.template;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;

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
     * @return
     */
    BloomFilterTaskResult build(BloomFilterTask bloomFilterTask);

}
