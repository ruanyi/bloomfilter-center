package com.mifish.bloomfilter.center.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 17:21
 */
public class BloomFilterTaskPlan implements Serializable {

    private static final long serialVersionUID = 2170133766230833906L;

    /***taskId*/
    private final String taskId;

    /***originalTask*/
    private BloomFilterTask originalTask;

    /***optimizeTasks*/
    private List<BloomFilterTask> optimizeTasks = new ArrayList<>();

    /**
     * BloomFilterTaskPlan
     *
     * @param originalTask
     */
    public BloomFilterTaskPlan(BloomFilterTask originalTask) {
        this.taskId = UUID.randomUUID().toString();
        this.originalTask = originalTask;
    }

    /**
     * getTaskId
     *
     * @return
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * getOriginalTask
     *
     * @return
     */
    public BloomFilterTask getOriginalTask() {
        return originalTask;
    }

    /**
     * addPlanTask
     *
     * @param bftask
     * @return
     */
    public synchronized boolean addPlanTask(BloomFilterTask bftask) {
        if (bftask == null) {
            return false;
        }
        this.optimizeTasks.add(bftask);
        /**sort*/
        Collections.sort(this.optimizeTasks);
        return true;
    }

    /**
     * getOptimizeTasks
     *
     * @return
     */
    public List<BloomFilterTask> getOptimizeTasks() {
        return new ArrayList<>(this.optimizeTasks);
    }
}
