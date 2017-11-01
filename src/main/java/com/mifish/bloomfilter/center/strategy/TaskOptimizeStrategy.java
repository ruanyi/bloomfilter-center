package com.mifish.bloomfilter.center.strategy;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskPlan;
import com.mifish.bloomfilter.center.model.TaskMeta;

import java.util.List;

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
     * @param taskMetas
     * @return
     */
    BloomFilterTaskPlan optimize(BloomFilterTask bftask, List<TaskMeta> taskMetas);
}
