package com.mifish.bloomfilter.center.model;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

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
     * group
     */
    private String group;

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

    /***attributes*/
    private Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * preTask
     */
    private BloomFilterTask preTask;

    /**
     * nextTask
     */
    private BloomFilterTask nextTask;

    /**
     * BloomFilterTask
     *
     * @param group
     * @param bloomFilterName
     * @param order
     * @param taskType
     */
    private BloomFilterTask(String group, String bloomFilterName, int order, BloomFilterTaskType
            taskType) {
        this.group = group;
        this.bloomFilterName = bloomFilterName;
        this.order = order;
        this.taskType = taskType;
    }

    /**
     * getGroup
     *
     * @return
     */
    public String getGroup() {
        return group;
    }

    /**
     * getBloomFilterName
     *
     * @return
     */
    public String getBloomFilterName() {
        return bloomFilterName;
    }

    /**
     * getOrder
     *
     * @return
     */
    public int getOrder() {
        return order;
    }

    /**
     * getTaskType
     *
     * @return
     */
    public BloomFilterTaskType getTaskType() {
        return taskType;
    }

    /**
     * addAttribute
     *
     * @param key
     * @param value
     */
    public void addAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    /**
     * addAttributes
     *
     * @param attributes
     */
    public void addAttributes(Map<String, Object> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return;
        }
        this.attributes.putAll(attributes);
    }

    /**
     * getAttribute
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getAttribute(String key, Class<T> clazz) {
        Object obj = this.attributes.get(key);
        if (obj == null) {
            return null;
        }
        return clazz.cast(obj);
    }

    /**
     * setPreTask
     *
     * @param preTask
     */
    public void setPreTask(BloomFilterTask preTask) {
        this.preTask = preTask;
    }

    /**
     * setNextTask
     *
     * @param nextTask
     */
    public void setNextTask(BloomFilterTask nextTask) {
        this.nextTask = nextTask;
    }

    /**
     * getPreTask
     *
     * @return
     */
    public BloomFilterTask getPreTask() {
        return preTask;
    }

    /**
     * getNextTask
     *
     * @return
     */
    public BloomFilterTask getNextTask() {
        return nextTask;
    }

    /**
     * 是否强制构建任务
     *
     * @return
     */
    public boolean isForceBuildTask() {
        return getTaskType() == BloomFilterTaskType.FORCE_BUILD_TASK;
    }

    /**
     * isBuildTask
     *
     * @return
     */
    public boolean isBuildTask() {
        return getTaskType() == BloomFilterTaskType.FORCE_BUILD_TASK
                || getTaskType() == BloomFilterTaskType.NORMAL_BUILD_TASK
                || getTaskType() == BloomFilterTaskType.SUB_BUILD_TASK;
    }

    /**
     * isLoadTask
     *
     * @return
     */
    public boolean isLoadTask() {
        return getTaskType() == BloomFilterTaskType.NORMAL_LOAD_TASK
                || getTaskType() == BloomFilterTaskType.FORCE_LOAD_TASK
                || getTaskType() == BloomFilterTaskType.SUB_LOAD_TASK;
    }

    /**
     * 是否强制加载任务
     *
     * @return
     */
    public boolean isForceLoadTask() {
        return getTaskType() == BloomFilterTaskType.FORCE_LOAD_TASK;
    }

    /**
     * 是否强制任务
     *
     * @return
     */
    public boolean isForceTask() {
        return isBuildTask() || isForceLoadTask();
    }

    /**
     * isSubTask
     *
     * @return
     */
    public boolean isSubTask() {
        return getTaskType() == BloomFilterTaskType.SUB_LOAD_TASK
                || getTaskType() == BloomFilterTaskType.SUB_BUILD_TASK;
    }

    /**
     * buildSubTask
     *
     * @param group
     * @param bloomFilterName
     * @param order
     * @param subTaskType
     * @return
     */
    public static BloomFilterTask buildSubTask(String group, String bloomFilterName, int order,
                                               BloomFilterTaskType subTaskType) {
        if (StringUtils.isBlank(group) || StringUtils.isBlank(bloomFilterName) || order < 0) {
            return null;
        }
        if (subTaskType == BloomFilterTaskType.SUB_BUILD_TASK || subTaskType == BloomFilterTaskType.SUB_LOAD_TASK) {
            return new BloomFilterTask(group, bloomFilterName, order, subTaskType);
        }
        return null;
    }

    /**
     * buildStartUpLoadTask
     *
     * @param group
     * @param bloomFilterName
     * @param order
     * @return
     */
    public static BloomFilterTask buildStartUpLoadTask(String group, String bloomFilterName, int order) {
        if (StringUtils.isBlank(group) || StringUtils.isBlank(bloomFilterName) || order < 0) {
            return null;
        }
        return new BloomFilterTask(group, bloomFilterName, order, BloomFilterTaskType.STARTUP_LOAD_TASK);
    }

    /**
     * buildNormalLoadTask
     *
     * @param group
     * @param bloomFilterName
     * @param order
     * @return
     */
    public static BloomFilterTask buildNormalLoadTask(String group, String bloomFilterName, int order) {
        if (StringUtils.isBlank(group) || StringUtils.isBlank(bloomFilterName) || order < 0) {
            return null;
        }
        return new BloomFilterTask(group, bloomFilterName, order, BloomFilterTaskType.NORMAL_LOAD_TASK);
    }

    /**
     * buildForceLoadTask
     *
     * @param group
     * @param bloomFilterName
     * @param order
     * @return
     */
    public static BloomFilterTask buildForceLoadTask(String group, String bloomFilterName, int order) {
        if (StringUtils.isBlank(group) || StringUtils.isBlank(bloomFilterName) || order < 0) {
            return null;
        }
        return new BloomFilterTask(group, bloomFilterName, order, BloomFilterTaskType.FORCE_LOAD_TASK);
    }

    /**
     * buildNormalBuildTask
     *
     * @param group
     * @param bloomFilterName
     * @param order
     * @return
     */
    public static BloomFilterTask buildNormalBuildTask(String group, String bloomFilterName, int order) {
        if (StringUtils.isBlank(group) || StringUtils.isBlank(bloomFilterName) || order < 0) {
            return null;
        }
        return new BloomFilterTask(group, bloomFilterName, order, BloomFilterTaskType.NORMAL_BUILD_TASK);
    }

    /**
     * buildForceBuildTask
     *
     * @param group
     * @param bloomFilterName
     * @param order
     * @return
     */
    public static BloomFilterTask buildForceBuildTask(String group, String bloomFilterName, int order) {
        if (StringUtils.isBlank(group) || StringUtils.isBlank(bloomFilterName) || order < 0) {
            return null;
        }
        return new BloomFilterTask(group, bloomFilterName, order, BloomFilterTaskType.FORCE_BUILD_TASK);
    }

    /**
     * compareTo
     *
     * @param bftask
     * @return
     */
    @Override
    public int compareTo(BloomFilterTask bftask) {
        checkArgument(bftask != null, "BloomFilterTask cannot be null");
        return (getOrder() < bftask.getOrder()) ? -1 : (getOrder() == bftask.getOrder()) ? 0 : 1;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("BloomFilterTask[");
        result.append("[group=").append(this.group).append(",name=").append(bloomFilterName).append(",order=").append
                (order).append(",taskType=").append(this.taskType).append("]");
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BloomFilterTask that = (BloomFilterTask) o;

        if (order != that.order) {
            return false;
        }
        if (group != null ? !group.equals(that.group) : that.group != null) {
            return false;
        }
        if (bloomFilterName != null ? !bloomFilterName.equals(that.bloomFilterName) : that.bloomFilterName != null) {
            return false;
        }
        if (taskType != that.taskType) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = group != null ? group.hashCode() : 0;
        result = 31 * result + (bloomFilterName != null ? bloomFilterName.hashCode() : 0);
        result = 31 * result + order;
        result = 31 * result + (taskType != null ? taskType.hashCode() : 0);
        return result;
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
    public enum BloomFilterTaskType {

        NORMAL_BUILD_TASK,

        FORCE_BUILD_TASK,

        SUB_BUILD_TASK,

        STARTUP_LOAD_TASK,

        NORMAL_LOAD_TASK,

        SUB_LOAD_TASK,

        FORCE_LOAD_TASK
    }
}
