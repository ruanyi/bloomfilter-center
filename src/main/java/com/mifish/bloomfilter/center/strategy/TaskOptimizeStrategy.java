package com.mifish.bloomfilter.center.strategy;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskPlan;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 17:20
 */
public interface TaskOptimizeStrategy {

    /**
     * optimize
     *
     * @param bftask
     * @return
     */
    BloomFilterTaskPlan optimize(BloomFilterTask bftask);
}
