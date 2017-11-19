package com.mifish.bloomfilter.center.worker.builder;

import com.mifish.bloomfilter.center.controller.BloomFilterController;
import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskPlan;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.model.BloomFilterWrapper;
import com.mifish.bloomfilter.center.model.ConfigMeta;
import com.mifish.bloomfilter.center.repository.BloomFilterConfigRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterLockRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterOutputRepository;
import com.mifish.bloomfilter.center.serializer.BloomFilterSerializer;
import com.mifish.bloomfilter.center.serializer.impl.SimpleBloomFilterSerializer;
import com.mifish.bloomfilter.center.template.BloomFilterBuildTemplate;
import com.mifish.bloomfilter.center.worker.AbstractBloomFilterTaskWorker;
import com.mifish.bloomfilter.center.worker.GroupTaskWorkerManager;
import com.mifish.bloomfilter.center.worker.TaskWorkerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mifish.bloomfilter.center.util.BloomFilterUtils.formateBloomFilterDate;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 15:37
 */
public class SimpleBloomFilterTaskBuilder extends AbstractBloomFilterTaskWorker {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(SimpleBloomFilterTaskBuilder.class);

    /***bloomFilterBuildTemplate*/
    private BloomFilterBuildTemplate bloomFilterBuildTemplate;

    /***bloomFilterController*/
    private BloomFilterController bloomFilterController;

    /***bloomFilterSerializer*/
    private BloomFilterSerializer bloomFilterSerializer = SimpleBloomFilterSerializer.getInstance();

    /**
     * AbstractBloomFilterTaskWorker
     *
     * @param taskWorkerType
     * @param group
     * @param taskWorkerManager
     */
    public SimpleBloomFilterTaskBuilder(TaskWorkerType taskWorkerType, String group, GroupTaskWorkerManager
            taskWorkerManager) {
        super(taskWorkerType, group, taskWorkerManager);
    }

    /**
     * executeTaskPlan
     *
     * @param taskPlan
     */
    @Override
    protected void executeTaskPlan(BloomFilterTaskPlan taskPlan) {
        long startTime = System.currentTimeMillis();
        if (taskPlan == null) {
            return;
        }
        //point task start time
        Date startTaskTime = new Date();
        List<BloomFilterTask> buildAllTasks = taskPlan.getOptimizeTasks();
        Map<String, BloomFilterWrapper> bfs = new HashMap<>(buildAllTasks.size());
        Map<String, Date> bflocks = new HashMap<>(buildAllTasks.size());
        boolean isAllSuccess = true;
        String taskId = taskPlan.getTaskId();
        try {
            for (BloomFilterTask buildTask : buildAllTasks) {
                String bfname = buildTask.getBloomFilterName();
                buildTask.addAttribute("taskId", taskId);
                ConfigMeta configMeta = obtainBloomFilterConfigMeta(taskId, bfname);
                buildTask.addAttribute("config_meta", configMeta);
                Date timeVersion = new Date();
                //the diffierent beetween timeVersion and startTaskTime
                buildTask.addAttribute("startTaskTime", startTaskTime);
                buildTask.addAttribute("timeVersion", timeVersion);
                //lock
                boolean isLock = obtainBloomFilterBuildLock(taskId, bfname, timeVersion,
                        buildTask.isForceBuildTask());
                if (isLock) {
                    bflocks.put(bfname, timeVersion);
                    BloomFilterTaskResult result = this.bloomFilterBuildTemplate.build(buildTask);
                    if (result == null || !result.isSuccess()) {
                        isAllSuccess = false;
                        break;
                    }
                    //
                    bfs.put(bfname, result.getBloomFilterWrapper());
                }
            }
            //
            if (isAllSuccess && !bfs.isEmpty()) {
                isAllSuccess = consistentStoreBloomFilters(taskId, bfs);
            }
        } finally {
            //释放刚刚获得的锁
            releaseBloomFilterBuildLock(taskId, bflocks);
            //logger detail message
            logBloomFilterDetailMessage(taskPlan, isAllSuccess, startTime);
        }

    }

    /**
     * obtainBloomFilterConfigMeta
     *
     * @param taskId
     * @param bfname
     * @return
     */
    private ConfigMeta obtainBloomFilterConfigMeta(String taskId, String bfname) {
        BloomFilterConfigRepository bloomFilterConfigRepository = this.bloomFilterBuildTemplate
                .getBloomFilterConfigRepository();
        ConfigMeta configMeta = bloomFilterConfigRepository.queryConfigMetaByName(bfname);
        if (logger.isInfoEnabled()) {
            logger.info("SimpleBloomFilterTaskBuilder,obtainBloomFilterConfigMeta,taskId:" + taskId + ",name:" + bfname
                    + ",configMeta:" + configMeta);
        }
        return configMeta;
    }

    /**
     * releaseBloomFilterBuildLock
     *
     * @param taskId
     * @param bflocks
     */
    private boolean releaseBloomFilterBuildLock(String taskId, Map<String, Date> bflocks) {
        if (bflocks == null || bflocks.isEmpty()) {
            return false;
        }
        BloomFilterLockRepository bloomFilterLockRepository = this.bloomFilterBuildTemplate
                .getBloomFilterLockRepository();
        Map<String, Boolean> status = new HashMap<>(bflocks.size());
        for (Map.Entry<String, Date> entry : bflocks.entrySet()) {
            boolean isSuccess = bloomFilterLockRepository.releaseBloomFilterLock(entry.getKey(), entry.getValue());
            status.put(entry.getKey(), isSuccess);
        }
        if (logger.isInfoEnabled()) {
            logger.info("SimpleBloomFilterTaskBuilder,releaseBloomFilterLoadLock,taskId:" + taskId + ",status:" +
                    status);
        }
        return true;
    }

    /**
     * consistentStoreBloomFilters
     *
     * @param taskId
     * @param bfs
     * @return
     */
    private boolean consistentStoreBloomFilters(String taskId, Map<String, BloomFilterWrapper> bfs) {
        if (bfs == null || bfs.isEmpty()) {
            return false;
        }
        BloomFilterOutputRepository bloomFilterOutputRepository = this.bloomFilterBuildTemplate
                .getBloomFilterOutputRepository();
        Collection<byte[]> cs = new ArrayList<>(bfs.size());
        for (Map.Entry<String, BloomFilterWrapper> entry : bfs.entrySet()) {
            byte[] bfbytes = this.bloomFilterSerializer.serialize(entry.getValue());
            cs.add(bfbytes);
        }
        boolean isSuccess = bloomFilterOutputRepository.batchStoreBloomFilters(cs);
        if (logger.isInfoEnabled()) {
            logger.info("SimpleBloomFilterTaskBuilder,consistentStoreBloomFilters,taskId:" + taskId + ",bfnames:" +
                    bfs.keySet() + "," + "isSuccess:" + isSuccess);
        }
        return isSuccess;
    }

    /**
     * obtainBloomFilterBuildLock
     *
     * @param taskId
     * @param bfname
     * @param timeVersion
     * @param isForced
     * @return
     */
    private boolean obtainBloomFilterBuildLock(String taskId, String bfname, Date timeVersion, boolean isForced) {
        BloomFilterLockRepository bloomFilterLockRepository = this.bloomFilterBuildTemplate
                .getBloomFilterLockRepository();
        boolean isLock = bloomFilterLockRepository.obtainBloomFilterLock(bfname, timeVersion, isForced);
        if (logger.isInfoEnabled()) {
            logger.info("SimpleBloomFilterTaskBuilder,obtainBloomFilterLoadLock,taskId:" + taskId + ",name:" + bfname +
                    ",timeVersion:" + formateBloomFilterDate(timeVersion) + ",isForced:" + isForced + ",isLock:" +
                    isLock);
        }
        return isLock;
    }

    /**
     * logBloomFilterDetailMessage
     *
     * @param taskPlan
     * @param isAllSuccess
     * @param startTime
     */
    private void logBloomFilterDetailMessage(BloomFilterTaskPlan taskPlan, boolean isAllSuccess, long startTime) {
        if (logger.isInfoEnabled()) {
            logger.info("SimpleBloomFilterTaskBuilder,executeTaskPlan,isAllSuccess:" + isAllSuccess + ",taskId:" +
                    taskPlan.getTaskId() + ",oriBuildTask:" + taskPlan.getOriginalTask() + "allBuildTask:" + taskPlan
                    .getOptimizeTasks() + ",cost:" +
                    (System.currentTimeMillis() -
                            startTime));
        }
    }

    /**
     * setBloomFilterBuildTemplate
     *
     * @param bloomFilterBuildTemplate
     */
    public void setBloomFilterBuildTemplate(BloomFilterBuildTemplate bloomFilterBuildTemplate) {
        this.bloomFilterBuildTemplate = bloomFilterBuildTemplate;
    }

    /**
     * setBloomFilterController
     *
     * @param bloomFilterController
     */
    public void setBloomFilterController(BloomFilterController bloomFilterController) {
        this.bloomFilterController = bloomFilterController;
    }
}
