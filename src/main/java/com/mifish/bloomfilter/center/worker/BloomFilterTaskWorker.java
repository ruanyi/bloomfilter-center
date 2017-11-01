package com.mifish.bloomfilter.center.worker;

import com.mifish.bloomfilter.center.model.BloomFilterTask;

/**
 * Description:
 * <p>
 * 一个组：group由统一的工作者来处理
 *
 * @author : rls
 * Date: 2017-10-13 23:39
 */
public interface BloomFilterTaskWorker {

    /**
     * group
     *
     * @return
     */
    String group();

    /**
     * submitTask
     *
     * @param task
     * @return
     */
    boolean submitTask(BloomFilterTask task);

    /**
     * getTaskWorkerType
     *
     * @return
     */
    TaskWorkerType getTaskWorkerType();


    /**
     * getTaskWorkerManager
     *
     * @return
     */
    GroupTaskWorkerManager getTaskWorkerManager();


}
