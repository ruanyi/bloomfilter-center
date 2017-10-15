package com.mifish.bloomfilter.center.worker;

import com.mifish.bloomfilter.center.model.BloomFilterTask;

/**
 * Description:
 * <p>
 * GroupTaskWorkerManager
 *
 * @author: rls
 * Date: 2017-10-15 11:07
 */
public interface GroupTaskWorkerManager {

    /**
     * 到经理这边报道
     *
     * @param group
     * @param name
     * @param order
     * @return
     */
    BloomFilterTaskWorker report(String group, String name,int order);

    /**
     * allocateTask
     *
     * @param group
     * @param name
     * @return
     */
    BloomFilterTask allocateTask(String group, String name);

}
