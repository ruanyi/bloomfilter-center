package com.mifish.bloomfilter.center.worker.loader;

import com.mifish.bloomfilter.center.controller.BloomFilterController;
import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskPlan;
import com.mifish.bloomfilter.center.template.BloomFilterLoadTemplate;
import com.mifish.bloomfilter.center.worker.AbstractBloomFilterTaskWorker;
import com.mifish.bloomfilter.center.worker.GroupTaskWorkerManager;
import com.mifish.bloomfilter.center.worker.TaskWorkerType;
import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 11:31
 */
public class SimpleBloomFilterTaskLoader extends AbstractBloomFilterTaskWorker {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(SimpleBloomFilterTaskLoader.class);

    /***bloomFilterLoadTemplate*/
    private BloomFilterLoadTemplate bloomFilterLoadTemplate;

    /***bloomFilterController*/
    private BloomFilterController bloomFilterController;

    /***formatter*/
    private static final FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    /**
     * AbstractBloomFilterTaskWorker
     *
     * @param taskWorkerType
     * @param group
     * @param taskWorkerManager
     */
    public SimpleBloomFilterTaskLoader(TaskWorkerType taskWorkerType, String group, GroupTaskWorkerManager
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
        List<BloomFilterTask> loadAllTasks = taskPlan.getOptimizeTasks();
        for (BloomFilterTask bftask : loadAllTasks) {
            this.bloomFilterLoadTemplate.load(bftask, startTaskTime);
        }
    }

    public void setBloomFilterLoadTemplate(BloomFilterLoadTemplate bloomFilterLoadTemplate) {
        this.bloomFilterLoadTemplate = bloomFilterLoadTemplate;
    }

    public void setBloomFilterController(BloomFilterController bloomFilterController) {
        this.bloomFilterController = bloomFilterController;
    }
}
