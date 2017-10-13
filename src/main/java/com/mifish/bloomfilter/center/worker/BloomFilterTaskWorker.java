package com.mifish.bloomfilter.center.worker;

import com.mifish.bloomfilter.center.model.BloomFilterTask;

/**
 * Description:
 * <p>
 * User: rls
 * Date: 2017-10-13 23:39
 */
public interface BloomFilterTaskWorker {

    /**
     * submitTask
     *
     * @param task
     * @return
     */
    boolean submitTask(BloomFilterTask task);
}
