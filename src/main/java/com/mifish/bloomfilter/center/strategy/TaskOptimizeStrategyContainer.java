package com.mifish.bloomfilter.center.strategy;

import com.mifish.bloomfilter.center.model.BloomFilterTask;

import java.util.Map;
import java.util.Set;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 17:39
 */
public interface TaskOptimizeStrategyContainer {

    /**
     * select
     * <p>
     * if null return default TaskOptimizeStrategy
     *
     * @param taskType
     * @return
     */
    TaskOptimizeStrategy select(BloomFilterTask.BloomFilterTaskType taskType);

    /**
     * isStrategyExists
     *
     * @param taskType
     * @return
     */
    boolean isStrategyExists(BloomFilterTask.BloomFilterTaskType taskType);

    /**
     * getAllTaskStrategys
     *
     * @return
     */
    Map<BloomFilterTask.BloomFilterTaskType, TaskOptimizeStrategy> getAllTaskStrategys();

    /**
     * getAllStrategyNames
     *
     * @return
     */
    Set<String> getAllStrategyNames();

    /**
     * addOneStrategy
     *
     * @param taskType
     * @param strategy
     * @return
     */
    boolean addOneStrategy(BloomFilterTask.BloomFilterTaskType taskType, TaskOptimizeStrategy strategy);

    /**
     * remove
     *
     * @param taskType
     * @return
     */
    TaskOptimizeStrategy remove(BloomFilterTask.BloomFilterTaskType taskType);

}
