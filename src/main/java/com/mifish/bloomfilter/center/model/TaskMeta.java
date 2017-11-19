package com.mifish.bloomfilter.center.model;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-01 22:52
 */
public class TaskMeta implements Serializable, Comparable<TaskMeta> {

    /***taskName*/
    private String taskName;

    /***taskOrder*/
    private int taskOrder;

    /**
     * TaskMeta
     *
     * @param taskName
     * @param taskOrder
     */
    public TaskMeta(String taskName, int taskOrder) {
        this.taskName = taskName;
        this.taskOrder = taskOrder;
    }

    /**
     * getTaskName
     *
     * @return
     */
    public String getTaskName() {
        return this.taskName;
    }

    /**
     * getTaskOrder
     *
     * @return
     */
    public int getTaskOrder() {
        return taskOrder;
    }

    @Override
    public int compareTo(TaskMeta other) {
        checkArgument(other != null, "TaskMeta cannot be null");
        return (getTaskOrder() < other.getTaskOrder()) ? -1 : (getTaskOrder() == other.getTaskOrder()) ? 0 : 1;
    }

    /**
     * toString
     *
     * @return
     */
    @Override
    public String toString() {
        return "taskName=" + taskName + ", taskOrder=" + taskOrder;
    }

    /**
     * equals
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskMeta taskMeta = (TaskMeta) o;

        if (taskOrder != taskMeta.taskOrder) {
            return false;
        }
        return taskName != null ? taskName.equals(taskMeta.taskName) : taskMeta.taskName == null;
    }

    /**
     * hashCode
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = taskName != null ? taskName.hashCode() : 0;
        result = 31 * result + taskOrder;
        return result;
    }
}
