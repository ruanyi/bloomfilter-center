package com.mifish.bloomfilter.center.strategy.container;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.strategy.TaskOptimizeStrategy;
import com.mifish.bloomfilter.center.strategy.TaskOptimizeStrategyContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 21:36
 */
public class SimpleTaskOptimizeStrategyContainer implements TaskOptimizeStrategyContainer {

    /***strategyContainer*/
    private Map<BloomFilterTask.BloomFilterTaskType, TaskOptimizeStrategy> strategyContainer = new
            ConcurrentHashMap<>();

    /**
     * select
     *
     * @param taskType
     * @return
     */
    @Override
    public TaskOptimizeStrategy select(BloomFilterTask.BloomFilterTaskType taskType) {
        return this.strategyContainer.get(taskType);
    }

    /**
     * isStrategyExists
     *
     * @param taskType
     * @return
     */
    @Override
    public boolean isStrategyExists(BloomFilterTask.BloomFilterTaskType taskType) {
        return this.strategyContainer.containsKey(taskType);
    }

    /**
     * getAllTaskStrategys
     *
     * @return
     */
    @Override
    public Map<BloomFilterTask.BloomFilterTaskType, TaskOptimizeStrategy> getAllTaskStrategys() {
        return new HashMap<>(this.strategyContainer);
    }

    /**
     * getAllStrategyNames
     *
     * @return
     */
    @Override
    public Set<String> getAllStrategyNames() {
        Set<BloomFilterTask.BloomFilterTaskType> taskTypes = this.strategyContainer.keySet();
        Set<String> strategyNames = new HashSet<>(taskTypes.size());
        for (BloomFilterTask.BloomFilterTaskType taskType : taskTypes) {
            strategyNames.add(taskType.name());
        }
        return strategyNames;
    }

    /**
     * addOneStrategy
     *
     * @param taskType
     * @param strategy
     * @return
     */
    @Override
    public boolean addOneStrategy(BloomFilterTask.BloomFilterTaskType taskType, TaskOptimizeStrategy strategy) {
        this.strategyContainer.put(taskType, strategy);
        return true;
    }

    /**
     * remove
     *
     * @param taskType
     * @return
     */
    @Override
    public TaskOptimizeStrategy remove(BloomFilterTask.BloomFilterTaskType taskType) {
        return this.strategyContainer.remove(taskType);
    }
}
