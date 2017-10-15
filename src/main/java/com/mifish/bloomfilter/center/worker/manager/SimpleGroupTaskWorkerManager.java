package com.mifish.bloomfilter.center.worker.manager;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.worker.BloomFilterTaskWorker;
import com.mifish.bloomfilter.center.worker.GroupTaskWorkerManager;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 11:21
 */
public class SimpleGroupTaskWorkerManager implements GroupTaskWorkerManager {

    /***workers*/
    private ConcurrentHashMap<String, BloomFilterGroupTaskWorker> workers = new ConcurrentHashMap<>();

    /***lock*/
    private Lock lock = new ReentrantLock();

    @Override
    public BloomFilterTaskWorker report(String group, String name, int order) {
        if (this.workers.containsKey(group)) {
            BloomFilterGroupTaskWorker groupTaskWorker = this.workers.get(group);
            if (groupTaskWorker.isContainTask(name)) {
                return groupTaskWorker.getBloomFilterTaskWorker();
            } else {
                groupTaskWorker.addTask(name, order);
            }
        }
        return null;
    }

    /**
     * allocateTask
     *
     * @param group
     * @param name
     * @param isForced
     * @return
     */
    @Override
    public BloomFilterTask allocateTask(String group, String name, boolean isForced) {
        return null;
    }

    /**
     * Description:
     *
     * @author: rls
     * Date: 2017-10-15 11:21
     */
    private static class BloomFilterGroupTaskWorker {

        /***group */
        private String group;

        /***bloomFilterTaskWorker*/
        private BloomFilterTaskWorker bloomFilterTaskWorker;

        /***taskMetas */
        private List<TaskMeta> taskMetas = new ArrayList<>();

        /***getBloomFilterTaskWorker*/
        public BloomFilterTaskWorker getBloomFilterTaskWorker() {
            return bloomFilterTaskWorker;
        }

        /**
         * BloomFilterGroupTaskWorker
         *
         * @param group
         * @param bloomFilterTaskWorker
         */
        public BloomFilterGroupTaskWorker(String group, BloomFilterTaskWorker bloomFilterTaskWorker) {
            this.group = group;
            this.bloomFilterTaskWorker = bloomFilterTaskWorker;
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
         * isContainTask
         *
         * @param name
         * @return
         */
        public boolean isContainTask(String name) {
            return this.taskMetas.contains(name);
        }

        /**
         * addTask
         *
         * @param taskName
         * @param taskOrder
         * @return
         */
        public synchronized boolean addTask(String taskName, int taskOrder) {
            if (StringUtils.isBlank(taskName) || taskOrder < 0) {
                return false;
            }
            if (!isContainTask(taskName)) {
                TaskMeta taskMeta = new TaskMeta(taskName, taskOrder);
                this.taskMetas.add(taskMeta);
                return true;
            }
            return false;
        }
    }

    private static class TaskMeta implements Serializable, Comparable<TaskMeta> {

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

        public String getTaskName() {
            return taskName;
        }

        public int getTaskOrder() {
            return taskOrder;
        }

        @Override
        public int compareTo(TaskMeta other) {
            checkArgument(other != null, "TaskMeta cannot be null");
            return (getTaskOrder() < other.getTaskOrder()) ? -1 : (getTaskOrder() == other.getTaskOrder()) ? 0 : 1;
        }

        @Override
        public String toString() {
            return "taskName='" + taskName + '\'' +
                    ", taskOrder=" + taskOrder;
        }

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

        @Override
        public int hashCode() {
            int result = taskName != null ? taskName.hashCode() : 0;
            result = 31 * result + taskOrder;
            return result;
        }
    }
}
