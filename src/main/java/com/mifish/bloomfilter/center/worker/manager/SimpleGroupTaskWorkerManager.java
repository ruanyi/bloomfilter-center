package com.mifish.bloomfilter.center.worker.manager;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.worker.BloomFilterTaskWorker;
import com.mifish.bloomfilter.center.worker.GroupTaskWorkerManager;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 11:21
 */
public class SimpleGroupTaskWorkerManager implements GroupTaskWorkerManager {

    /**
     * workers
     */
    private ConcurrentHashMap<String, BloomFilterGroupTaskWorker> workers = new ConcurrentHashMap<>();

    @Override
    public BloomFilterTaskWorker report(String group, String name, int order) {

        if (this.workers.containsKey(group)) {
            BloomFilterGroupTaskWorker groupTaskWorker = this.workers.get(group);
            if (groupTaskWorker.isContainTask(name)) {
                return groupTaskWorker.getBloomFilterTaskWorker();
            }

        }
        return null;
    }

    @Override
    public BloomFilterTask allocateTask(String group, String name) {
        return null;
    }

    private static class BloomFilterGroupTaskWorker {

        /**
         * bloomFilterTaskWorker
         */
        private BloomFilterTaskWorker bloomFilterTaskWorker;

        /**
         * group
         */
        private String group;

        /**
         * taskrelation
         */
        private List<String> taskrelation = new LinkedList<>();

        public BloomFilterTaskWorker getBloomFilterTaskWorker() {
            return bloomFilterTaskWorker;
        }

        public boolean isContainTask(String name) {
            return this.taskrelation.contains(name);
        }

        public synchronized boolean addTask(String name, int order) {
            return false;
        }
    }

    private static class TaskOrderEntry{

        /**group*/
        private String group;

        /**name*/
        private String name;

        /**order*/
        private int order;



    }
}
