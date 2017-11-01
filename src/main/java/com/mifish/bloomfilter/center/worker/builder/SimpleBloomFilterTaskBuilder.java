package com.mifish.bloomfilter.center.worker.builder;

import com.mifish.bloomfilter.center.controller.BloomFilterController;
import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskPlan;
import com.mifish.bloomfilter.center.template.BloomFilterBuildTemplate;
import com.mifish.bloomfilter.center.worker.AbstractBloomFilterTaskWorker;
import com.mifish.bloomfilter.center.worker.GroupTaskWorkerManager;
import com.mifish.bloomfilter.center.worker.TaskWorkerType;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 15:37
 */
public class SimpleBloomFilterTaskBuilder extends AbstractBloomFilterTaskWorker {

    /***bloomFilterBuildTemplate*/
    private BloomFilterBuildTemplate bloomFilterBuildTemplate;

    /***bloomFilterController*/
    private BloomFilterController bloomFilterController;

    /**
     * AbstractBloomFilterTaskWorker
     *
     * @param taskWorkerType
     * @param group
     * @param taskWorkerManager
     */
    public SimpleBloomFilterTaskBuilder(TaskWorkerType taskWorkerType, String group, GroupTaskWorkerManager
            taskWorkerManager) {
        super(taskWorkerType, group, taskWorkerManager);
    }

    /**
     * executeTaskPlan
     *
     * @param taskPlan
     */
    @Override
    protected void executeTaskPlan(BloomFilterTaskPlan taskPlan) {
        if (taskPlan == null) {
            return;
        }
        Date startTaskTime = new Date();
        List<BloomFilterTask> buildAllTasks = taskPlan.getOptimizeTasks();
        for (BloomFilterTask bftask : buildAllTasks) {
            this.bloomFilterBuildTemplate.build(bftask, startTaskTime);
        }
    }

    public void setBloomFilterBuildTemplate(BloomFilterBuildTemplate bloomFilterBuildTemplate) {
        this.bloomFilterBuildTemplate = bloomFilterBuildTemplate;
    }

    public void setBloomFilterController(BloomFilterController bloomFilterController) {
        this.bloomFilterController = bloomFilterController;
    }
}
