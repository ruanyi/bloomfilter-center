package com.mifish.bloomfilter.center.model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * <p>
 *
 * @author : rls
 * Date: 2017-10-13 23:39
 */
public class BloomFilterTask implements Serializable, Comparable<BloomFilterTask> {

    private static final long serialVersionUID = -8851005177130349239L;

    /**
     * taskId
     */
    private String taskId;

    /**
     * bloomFilterName
     */
    private String bloomFilterName;

    /**
     * order
     */
    private int order;

    /**
     * taskType
     */
    private BloomFilterTaskType taskType;

    /**
     * dependSubTask
     */
    private List<BloomFilterTask> dependSubTask = Lists.newArrayList();


    @Override
    public int compareTo(BloomFilterTask o) {
        return 0;
    }

    /**
     * Description:
     * <p>
     * BloomFilterTaskType
     * <p>
     * <p>
     *
     * @author : rls
     * Date: 2017-10-13 23:39
     */
    public static enum BloomFilterTaskType {


        NORMAL_BUILD_TASK,

        FORCE_BUILD_TASK,

        SUB_BUILD_TASK,

        STARTUP_LOAD_TASK,

        NORMAL_LOAD_TASK,

        SUB_LOAD_TASK,

        FORCE_LOAD_TASK;
    }
}
