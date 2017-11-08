package com.mifish.bloomfilter.center.worker.builder;

import com.mifish.bloomfilter.center.controller.BloomFilterController;
import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskPlan;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.model.BloomFilterWrapper;
import com.mifish.bloomfilter.center.template.BloomFilterBuildTemplate;
import com.mifish.bloomfilter.center.worker.AbstractBloomFilterTaskWorker;
import com.mifish.bloomfilter.center.worker.GroupTaskWorkerManager;
import com.mifish.bloomfilter.center.worker.TaskWorkerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 15:37
 */
public class SimpleBloomFilterTaskBuilder extends AbstractBloomFilterTaskWorker {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(SimpleBloomFilterTaskBuilder.class);

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
        long startTime = System.currentTimeMillis();
        if (taskPlan == null) {
            return;
        }
        Date timeVersion = new Date();
        List<BloomFilterTask> buildAllTasks = taskPlan.getOptimizeTasks();
        Map<String, BloomFilterWrapper> bfs = new HashMap<>(buildAllTasks.size());
        Map<String, Date> bflocks = new HashMap<>(buildAllTasks.size());
        boolean isAllSuccess = true;
        String taskId = taskPlan.getTaskId();
        try {
            for (BloomFilterTask buildTask : buildAllTasks) {
                String bfname = buildTask.getBloomFilterName();
                buildTask.addAttribute("taskId", taskId);
                buildTask.addAttribute("timeVersion", timeVersion);
                boolean isLock = obtainBloomFilterBuildLock(taskId, bfname, timeVersion,
                        buildTask.isForceBuildTask());
                if (isLock) {
                    bflocks.put(bfname, timeVersion);
                    BloomFilterTaskResult result = this.bloomFilterBuildTemplate.build(buildTask);
                    if (result == null || !result.isSuccess()) {
                        isAllSuccess = false;
                        break;
                    }
                    //
                    bfs.put(bfname, result.getBloomFilterWrapper());
                }
            }
            //
            if (isAllSuccess && !bfs.isEmpty()) {
                isAllSuccess = consistentStoreBloomFilters(taskId, bfs);
            }
        } finally {
            //释放刚刚获得的锁
            releaseBloomFilterBuildLock(taskId, bflocks);
            //logger detail message
            logBloomFilterDetailMessage(taskPlan, isAllSuccess, startTime);
        }

    }

    /**
     * releaseBloomFilterBuildLock
     *
     * @param taskId
     * @param bflocks
     */
    private void releaseBloomFilterBuildLock(String taskId, Map<String, Date> bflocks) {

    }

    /**
     * consistentStoreBloomFilters
     *
     * @param taskId
     * @param bfs
     * @return
     */
    private boolean consistentStoreBloomFilters(String taskId, Map<String, BloomFilterWrapper> bfs) {
        return false;
    }

    /**
     * obtainBloomFilterBuildLock
     *
     * @param taskId
     * @param bfname
     * @param timeVersion
     * @param forceBuildTask
     * @return
     */
    private boolean obtainBloomFilterBuildLock(String taskId, String bfname, Date timeVersion, boolean forceBuildTask) {
        return false;
    }

    /**
     * logBloomFilterDetailMessage
     *
     * @param taskPlan
     * @param isAllSuccess
     * @param startTime
     */
    private void logBloomFilterDetailMessage(BloomFilterTaskPlan taskPlan, boolean isAllSuccess, long startTime) {

        if (logger.isInfoEnabled()) {
            logger.info("");
        }
    }

    /**
     * setBloomFilterBuildTemplate
     *
     * @param bloomFilterBuildTemplate
     */
    public void setBloomFilterBuildTemplate(BloomFilterBuildTemplate bloomFilterBuildTemplate) {
        this.bloomFilterBuildTemplate = bloomFilterBuildTemplate;
    }

    /**
     * setBloomFilterController
     *
     * @param bloomFilterController
     */
    public void setBloomFilterController(BloomFilterController bloomFilterController) {
        this.bloomFilterController = bloomFilterController;
    }
}
