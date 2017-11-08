package com.mifish.bloomfilter.center.worker.loader;

import com.mifish.bloomfilter.center.BloomFilterConstant;
import com.mifish.bloomfilter.center.container.BloomFilterContainer;
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
import com.mifish.bloomfilter.center.template.BloomFilterLoadTemplate;
import com.mifish.bloomfilter.center.worker.AbstractBloomFilterTaskWorker;
import com.mifish.bloomfilter.center.worker.GroupTaskWorkerManager;
import com.mifish.bloomfilter.center.worker.TaskWorkerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mifish.bloomfilter.center.BloomFilterConstant.BIT_FACTOR;
import static com.mifish.bloomfilter.center.util.BloomFilterUtils.formateBloomFilterDate;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 11:31
 */
public class SimpleBloomFilterTaskLoader extends AbstractBloomFilterTaskWorker {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(SimpleBloomFilterTaskLoader.class);

    /***bloomFilterLoadTemplate*/
    private BloomFilterLoadTemplate bloomFilterLoadTemplate;

    /***bloomFilterContainer*/
    private BloomFilterContainer bloomFilterContainer;

    /***bloomFilterSerializer*/
    private BloomFilterSerializer bloomFilterSerializer = SimpleBloomFilterSerializer.getInstance();

    /**
     * AbstractBloomFilterTaskWorker
     *
     * @param taskWorkerType
     * @param group
     * @param taskWorkerManager
     */
    public SimpleBloomFilterTaskLoader(TaskWorkerType taskWorkerType, String group, GroupTaskWorkerManager
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
        List<BloomFilterTask> loadAllTasks = taskPlan.getOptimizeTasks();
        Map<String, BloomFilterWrapper> bfs = new HashMap<>(loadAllTasks.size());
        Map<String, ConfigMeta> bflocks = new HashMap<>(loadAllTasks.size());
        boolean isAllSuccess = true;
        String taskId = taskPlan.getTaskId();
        try {
            for (BloomFilterTask loadTask : loadAllTasks) {
                String bfname = loadTask.getBloomFilterName();
                ConfigMeta configMeta = obtainBloomFilterConfigMeta(taskId, bfname);
                loadTask.addAttribute("config_meta", configMeta);
                loadTask.addAttribute("taskId", taskId);
                boolean isLock = obtainBloomFilterLoadLock(taskId, bfname, configMeta.getTimeVersion(),
                        loadTask.isForceLoadTask());
                if (isLock) {
                    bflocks.put(bfname, configMeta);
                    BloomFilterTaskResult result = this.bloomFilterLoadTemplate.load(loadTask);
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
            releaseBloomFilterLoadLock(taskId, bflocks);
            //logger detail message
            logBloomFilterDetailMessage(taskPlan, isAllSuccess, startTime);
        }
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
        BloomFilterOutputRepository bloomFilterOutputRepository = this.bloomFilterLoadTemplate
                .getBloomFilterOutputRepository();
        Collection<byte[]> cs = new ArrayList<>(bfs.size());
        for (Map.Entry<String, BloomFilterWrapper> entry : bfs.entrySet()) {
            byte[] bfbytes = this.bloomFilterSerializer.serialize(entry.getValue());
            cs.add(bfbytes);
        }
        boolean isSuccess = bloomFilterOutputRepository.batchStoreBloomFilters(cs);
        if (logger.isInfoEnabled()) {
            logger.info("SimpleBloomFilterTaskLoader,consistentStoreBloomFilters,taskId:" + taskId + ",bfnames:" +
                    bfs.keySet() + "," + "isSuccess:" + isSuccess);
        }
        return isSuccess;
    }

    /**
     * logBloomFilterDetailMessage
     *
     * @param taskPlan
     * @param isAllSuccess
     * @param startTime
     */
    private void logBloomFilterDetailMessage(BloomFilterTaskPlan taskPlan, boolean isAllSuccess, long startTime) {
        StringBuilder result = new StringBuilder("SimpleBloomFilterTaskLoader,");
        result.append("taskId=").append(taskPlan.getTaskId()).append(",");
        result.append("orgLoadTask:").append(taskPlan.getOriginalTask()).append(",");
        result.append("loadAllTasks").append(taskPlan.getOptimizeTasks()).append(",");
        result.append("isAllSuccess:").append(isAllSuccess).append(",");
        Set<String> bfnames = this.bloomFilterContainer.getAllBloomFilterNames();
        result.append("count:").append(bfnames.size()).append(",");
        result.append("bloomfilters[");
        for (Iterator<String> itr = bfnames.iterator(); itr.hasNext(); ) {
            String bfname = itr.next();
            BloomFilterWrapper bfwapper = this.bloomFilterContainer.getBloomFilterByName(bfname);
            result.append("{").append(bfname).append(",");
            result.append(formateBloomFilterDate(bfwapper.getTimeVersion())).append(",");
            result.append(bfwapper.getExpectedNumberOfElements()).append(",");
            result.append(bfwapper.getNumberOfAddedElements()).append(",");
            result.append(bfwapper.getBloomfilterBucketLength()).append(",");
            result.append(formatBloomFilterDataslength(bfwapper.getNumberOfBits() / BIT_FACTOR)).append(",");
            result.append(formatBloomFilterDataslength(bfwapper.getBloomfilterFileSize())).append("}");
        }
        result.append(",cost:").append(System.currentTimeMillis() - startTime).append("ms");
        if (logger.isInfoEnabled()) {
            logger.info(result.toString());
        }
    }

    /**
     * formatBloomFilterDataslength
     *
     * @param numberOfBytes
     * @return
     */
    private String formatBloomFilterDataslength(long numberOfBytes) {
        if (numberOfBytes < BloomFilterConstant.K_FACTOR) {
            return numberOfBytes + "Byte";
        }
        if (numberOfBytes < BloomFilterConstant.M_FACTOR) {
            return (numberOfBytes / BloomFilterConstant.K_FACTOR) + "K";
        }
        return (numberOfBytes / BloomFilterConstant.M_FACTOR) + "M";
    }

    /**
     * obtainBloomFilterConfigMeta
     *
     * @param taskId
     * @param name
     * @return
     */
    private ConfigMeta obtainBloomFilterConfigMeta(String taskId, String name) {
        BloomFilterConfigRepository bloomFilterConfigRepository = this.bloomFilterLoadTemplate
                .getBloomFilterConfigRepository();
        ConfigMeta configMeta = bloomFilterConfigRepository.queryConfigMetaByName(name);
        if (logger.isInfoEnabled()) {
            logger.info("SimpleBloomFilterTaskLoader,obtainBloomFilterConfigMeta,taskId:" + taskId + ",name:" + name
                    + ",configMeta:" + configMeta);
        }
        return configMeta;
    }

    /**
     * obtainBloomFilterLoadLock
     *
     * @param taskId
     * @param name
     * @param timeVersion
     * @param isForced
     * @return
     */
    private boolean obtainBloomFilterLoadLock(String taskId, String name, Date timeVersion, boolean isForced) {
        BloomFilterLockRepository bloomFilterLockRepository = this.bloomFilterLoadTemplate
                .getBloomFilterLockRepository();
        boolean isLock = bloomFilterLockRepository.obtainBloomFilterLock(name, timeVersion, isForced);
        if (logger.isInfoEnabled()) {
            logger.info("SimpleBloomFilterTaskLoader,obtainBloomFilterLoadLock,taskId:" + taskId + ",name:" + name +
                    ",timeVersion:" + formateBloomFilterDate(timeVersion) + ",isForced:" + isForced + ",isLock:" +
                    isLock);
        }
        return isLock;
    }


    /**
     * releaseBloomFilterLoadLock
     *
     * @param taskId
     * @param bflocks
     * @return
     */
    private boolean releaseBloomFilterLoadLock(String taskId, Map<String, ConfigMeta> bflocks) {
        if (bflocks == null || bflocks.isEmpty()) {
            return false;
        }
        BloomFilterLockRepository bloomFilterLockRepository = this.bloomFilterLoadTemplate
                .getBloomFilterLockRepository();
        Map<String, Boolean> status = new HashMap<>(bflocks.size());
        for (Map.Entry<String, ConfigMeta> entry : bflocks.entrySet()) {
            boolean isSuccess = bloomFilterLockRepository.releaseBloomFilterLock(entry.getKey(), entry.getValue()
                    .getTimeVersion());
            status.put(entry.getKey(), isSuccess);
        }
        if (logger.isInfoEnabled()) {
            logger.info("SimpleBloomFilterTaskLoader,releaseBloomFilterLoadLock,taskId:" + taskId + ",status:" +
                    status);
        }
        return true;
    }

    /**
     * setBloomFilterLoadTemplate
     *
     * @param bloomFilterLoadTemplate
     */
    public void setBloomFilterLoadTemplate(BloomFilterLoadTemplate bloomFilterLoadTemplate) {
        this.bloomFilterLoadTemplate = bloomFilterLoadTemplate;
    }

    /**
     * setBloomFilterContainer
     *
     * @param bloomFilterContainer
     */
    public void setBloomFilterContainer(BloomFilterContainer bloomFilterContainer) {
        this.bloomFilterContainer = bloomFilterContainer;
    }

    /**
     * setBloomFilterSerializer
     *
     * @param bloomFilterSerializer
     */
    public void setBloomFilterSerializer(BloomFilterSerializer bloomFilterSerializer) {
        this.bloomFilterSerializer = bloomFilterSerializer;
    }
}
