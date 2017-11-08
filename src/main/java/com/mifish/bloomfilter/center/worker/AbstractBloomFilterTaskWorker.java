package com.mifish.bloomfilter.center.worker;

import com.google.common.collect.Queues;
import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskPlan;
import com.mifish.bloomfilter.center.strategy.TaskOptimizeStrategy;
import com.mifish.bloomfilter.center.strategy.TaskOptimizeStrategyContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static com.mifish.bloomfilter.center.BloomFilterConstant.DEFAULT_WORK_QUEUE_NUM;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 10:37
 */
public abstract class AbstractBloomFilterTaskWorker implements BloomFilterTaskWorker, InitializingBean {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(AbstractBloomFilterTaskWorker.class);

    /***group */
    private String group;

    /*** processQueueNum*/
    private int processQueueNum = DEFAULT_WORK_QUEUE_NUM;

    /***taskContainer*/
    private LinkedBlockingQueue<BloomFilterTask> taskContainer = Queues.newLinkedBlockingQueue(processQueueNum);

    /***exector*/
    private ExecutorService exector = Executors.newFixedThreadPool(1, (r) -> new Thread(r, group()));

    /***strategyContainer*/
    private TaskOptimizeStrategyContainer strategyContainer;

    /***taskWorkerType*/
    private TaskWorkerType taskWorkerType;

    /***taskWorkerManager*/
    private GroupTaskWorkerManager taskWorkerManager;

    /**
     * AbstractBloomFilterTaskWorker
     *
     * @param taskWorkerType
     * @param group
     * @param taskWorkerManager
     */
    public AbstractBloomFilterTaskWorker(TaskWorkerType taskWorkerType, String group, GroupTaskWorkerManager
            taskWorkerManager) {
        this.taskWorkerType = taskWorkerType;
        this.group = group;
        this.taskWorkerManager = taskWorkerManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.exector.submit(() -> runTask());
    }

    /***runTask*/
    private void runTask() {
        while (true) {
            try {
                BloomFilterTask oriBfTask = this.taskContainer.take();
                if (oriBfTask == null) {
                    continue;
                }
                TaskOptimizeStrategy strategy = this.strategyContainer.select(oriBfTask.getTaskType());
                //优化计划
                BloomFilterTaskPlan taskPlan = strategy.optimize(oriBfTask, this.taskWorkerManager.tellMetaTasks(this
                        .taskWorkerType, group()));
                //执行计划
                executeTaskPlan(taskPlan);
            } catch (Exception ex) {
                logger.error("BloomFilterTaskWorker,runTask,queue catch exception,please check out!", ex);
            }
        }
    }

    /**
     * 由子类去实现，执行计划优化，已经由策略层实现了
     *
     * @param taskPlan
     */
    protected abstract void executeTaskPlan(BloomFilterTaskPlan taskPlan);

    @Override
    public String group() {
        return this.group;
    }

    /**
     * 提交任务队列，假如当前任务队列已经满了，则会提交失败，由下次调度再次进行
     *
     * @param task
     * @return
     */
    @Override
    public boolean submitTask(BloomFilterTask task) {
        if (task == null) {
            return false;
        }
        boolean isSuccess = this.taskContainer.offer(task);
        if (!isSuccess) {
            logger.warn("BloomFilterTaskWorker,submitTask[" + task + "],task queue is too long,num tasks=[" +
                    taskContainer.size() + "],please wait for a moment.");
        }
        return isSuccess;
    }

    /**
     * getTaskWorkerType
     *
     * @return
     */
    @Override
    public TaskWorkerType getTaskWorkerType() {
        return this.taskWorkerType;
    }

    /**
     * getTaskWorkerManager
     *
     * @return
     */
    @Override
    public GroupTaskWorkerManager getTaskWorkerManager() {
        return this.taskWorkerManager;
    }

    /**
     * setStrategyContainer
     *
     * @param strategyContainer
     */
    public void setStrategyContainer(TaskOptimizeStrategyContainer strategyContainer) {
        this.strategyContainer = strategyContainer;
    }
}
