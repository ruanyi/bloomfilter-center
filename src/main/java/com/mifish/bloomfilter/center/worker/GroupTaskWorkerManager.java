package com.mifish.bloomfilter.center.worker;

import com.mifish.bloomfilter.center.model.TaskMeta;

import java.util.List;

/**
 * Description:
 * <p>
 *
 * @author: rls
 * Date: 2017-10-15 11:07
 */
public interface GroupTaskWorkerManager {

    /**
     * 到经理这边报道
     *
     * @param workerType
     * @param group
     * @param name
     * @param order
     * @return
     */
    boolean report(TaskWorkerType workerType, String group, String name, int order);

    /**
     * allocate
     *
     * @param workerType
     * @param group
     * @return
     */
    BloomFilterTaskWorker allocate(TaskWorkerType workerType, String group);

    /**
     * tellMetaTasks
     *
     * @param workerType
     * @param group
     * @return
     */
    List<TaskMeta> tellMetaTasks(TaskWorkerType workerType, String group);

}
