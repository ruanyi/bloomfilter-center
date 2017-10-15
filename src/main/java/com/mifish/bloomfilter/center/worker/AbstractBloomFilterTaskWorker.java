package com.mifish.bloomfilter.center.worker;

import com.google.common.collect.Queues;
import com.mifish.bloomfilter.center.model.BloomFilterTask;
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

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(AbstractBloomFilterTaskWorker.class);

    /**
     * group
     */
    private String group;

    /**
     * processQueueNum
     */
    private int processQueueNum = DEFAULT_WORK_QUEUE_NUM;

    /**
     * taskContainer
     */
    private LinkedBlockingQueue<BloomFilterTask> taskContainer = Queues.newLinkedBlockingQueue(processQueueNum);

    /**
     * exector
     */
    private ExecutorService exector = Executors.newFixedThreadPool(1, (r) -> new Thread(r, group()));


    @Override
    public void afterPropertiesSet() throws Exception {
        this.exector.submit(() -> runTask());
    }

    /**
     * runTask
     */
    private void runTask() {
        while (true) {
            try {
                BloomFilterTask orgBfTask = this.taskContainer.take();
                if (orgBfTask == null) {
                    continue;
                }

            } catch (Exception ex) {
                logger.error("", ex);
            }
        }
    }

    @Override
    public String group() {
        return this.group;
    }

    @Override
    public boolean submitTask(BloomFilterTask task) {
        if (task == null) {
            return false;
        }
        boolean isSuccess = this.taskContainer.offer(task);
        if (!isSuccess) {
            logger.warn("BloomFilterTaskWorker,submitTask[" + task + "],task queue is too long,num tasks=[" + taskContainer.size() + "],please wait for a moment.");
        }
        return isSuccess;
    }

}
