package com.mifish.bloomfilter.center.worker.manager;

import com.mifish.bloomfilter.center.container.BloomFilterContainer;
import com.mifish.bloomfilter.center.controller.BloomFilterController;
import com.mifish.bloomfilter.center.model.TaskMeta;
import com.mifish.bloomfilter.center.strategy.TaskOptimizeStrategyContainer;
import com.mifish.bloomfilter.center.template.BloomFilterBuildTemplate;
import com.mifish.bloomfilter.center.template.BloomFilterLoadTemplate;
import com.mifish.bloomfilter.center.worker.BloomFilterTaskWorker;
import com.mifish.bloomfilter.center.worker.GroupTaskWorkerManager;
import com.mifish.bloomfilter.center.worker.TaskWorkerType;
import com.mifish.bloomfilter.center.worker.builder.SimpleBloomFilterTaskBuilder;
import com.mifish.bloomfilter.center.worker.loader.SimpleBloomFilterTaskLoader;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private final Lock lock = new ReentrantLock();

    /***bloomFilterLoadTemplate*/
    private BloomFilterLoadTemplate bloomFilterLoadTemplate;

    /***bloomFilterBuildTemplate*/
    private BloomFilterBuildTemplate bloomFilterBuildTemplate;

    /***bloomFilterController*/
    private BloomFilterController bloomFilterController;

    /***strategyContainer*/
    private TaskOptimizeStrategyContainer strategyContainer;

    /***bloomFilterContainer*/
    private BloomFilterContainer bloomFilterContainer;

    /**
     * report
     *
     * @param workerType
     * @param group
     * @param name
     * @param order
     * @return
     */
    @Override
    public boolean report(TaskWorkerType workerType, String group, String name, int order) {
        String key = buildTaskWorkName(workerType, group);
        if (this.workers.containsKey(key)) {
            BloomFilterGroupTaskWorker groupTaskWorker = this.workers.get(key);
            if (!groupTaskWorker.isContainTask(name)) {
                groupTaskWorker.addTask(name, order);
            }
            return true;
        } else {
            try {
                lock.lock();
                if (!this.workers.containsKey(key)) {
                    BloomFilterGroupTaskWorker newWorker = new BloomFilterGroupTaskWorker(workerType, group, this);
                    this.workers.put(key, newWorker);
                }
                BloomFilterGroupTaskWorker groupTaskWorker = this.workers.get(key);
                if (!groupTaskWorker.isContainTask(name)) {
                    groupTaskWorker.addTask(name, order);
                }
                return true;
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * allocate
     *
     * @param workerType
     * @param group
     * @return
     */
    @Override
    public BloomFilterTaskWorker allocate(TaskWorkerType workerType, String group) {
        String key = buildTaskWorkName(workerType, group);
        BloomFilterGroupTaskWorker groupTaskWorker = this.workers.get(key);
        return groupTaskWorker.getBloomFilterTaskWorker();
    }

    /**
     * tellMetaTasks
     *
     * @param workerType
     * @param group
     * @return
     */
    @Override
    public List<TaskMeta> tellMetaTasks(TaskWorkerType workerType, String group) {
        String key = buildTaskWorkName(workerType, group);
        BloomFilterGroupTaskWorker groupTaskWorker = this.workers.get(key);
        return groupTaskWorker.getTaskMetas();
    }

    /**
     * buildTaskWorkName
     *
     * @param workerType
     * @param group
     * @return
     */
    private String buildTaskWorkName(TaskWorkerType workerType, String group) {
        return workerType.name() + ":" + group;
    }


    public void setBloomFilterLoadTemplate(BloomFilterLoadTemplate bloomFilterLoadTemplate) {
        this.bloomFilterLoadTemplate = bloomFilterLoadTemplate;
    }

    public void setBloomFilterBuildTemplate(BloomFilterBuildTemplate bloomFilterBuildTemplate) {
        this.bloomFilterBuildTemplate = bloomFilterBuildTemplate;
    }

    public void setBloomFilterController(BloomFilterController bloomFilterController) {
        this.bloomFilterController = bloomFilterController;
    }

    public void setStrategyContainer(TaskOptimizeStrategyContainer strategyContainer) {
        this.strategyContainer = strategyContainer;
    }

    public void setBloomFilterContainer(BloomFilterContainer bloomFilterContainer) {
        this.bloomFilterContainer = bloomFilterContainer;
    }

    public void setWorkers(ConcurrentHashMap<String, BloomFilterGroupTaskWorker> workers) {
        this.workers = workers;
    }

    /**
     * Description:
     *
     * @author: rls
     * Date: 2017-10-15 11:21
     */
    private class BloomFilterGroupTaskWorker {

        /***bloomFilterTaskWorker*/
        private BloomFilterTaskWorker bloomFilterTaskWorker;

        /***taskMetas*/
        private List<TaskMeta> taskMetas = new ArrayList<>();

        /***getBloomFilterTaskWorker*/
        public BloomFilterTaskWorker getBloomFilterTaskWorker() {
            return this.bloomFilterTaskWorker;
        }

        /**
         * BloomFilterGroupTaskWorker
         *
         * @param workerType
         * @param group
         * @param taskWorkerManager
         */
        public BloomFilterGroupTaskWorker(TaskWorkerType workerType, String group, GroupTaskWorkerManager
                taskWorkerManager) {
            if (workerType == TaskWorkerType.BUILDER) {
                SimpleBloomFilterTaskBuilder builder = new SimpleBloomFilterTaskBuilder(workerType, group,
                        taskWorkerManager);
                builder.setStrategyContainer(strategyContainer);
                builder.setBloomFilterController(bloomFilterController);
                builder.setBloomFilterBuildTemplate(bloomFilterBuildTemplate);
                this.bloomFilterTaskWorker = builder;
            } else if (workerType == TaskWorkerType.LOADER) {
                SimpleBloomFilterTaskLoader loader = new SimpleBloomFilterTaskLoader(workerType, group,
                        taskWorkerManager);
                loader.setStrategyContainer(strategyContainer);
                loader.setBloomFilterLoadTemplate(bloomFilterLoadTemplate);
                loader.setBloomFilterContainer(bloomFilterContainer);
                this.bloomFilterTaskWorker = loader;
            }
        }

        /**
         * getGroup
         *
         * @return
         */
        public String getGroup() {
            return this.bloomFilterTaskWorker.group();
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
                //排序
                Collections.sort(this.taskMetas);
                return true;
            }
            return false;
        }

        /**
         * getTaskMetas
         *
         * @return
         */
        public List<TaskMeta> getTaskMetas() {
            return new ArrayList<>(this.taskMetas);
        }
    }
}
