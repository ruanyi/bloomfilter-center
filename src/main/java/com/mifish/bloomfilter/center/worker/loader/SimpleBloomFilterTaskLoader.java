package com.mifish.bloomfilter.center.worker.loader;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskPlan;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.model.BloomFilterWrapper;
import com.mifish.bloomfilter.center.model.ConfigMeta;
import com.mifish.bloomfilter.center.repository.BloomFilterConfigRepository;
import com.mifish.bloomfilter.center.template.BloomFilterLoadTemplate;
import com.mifish.bloomfilter.center.worker.AbstractBloomFilterTaskWorker;
import com.mifish.bloomfilter.center.worker.GroupTaskWorkerManager;
import com.mifish.bloomfilter.center.worker.TaskWorkerType;
import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    /***formatter*/
    private static final FastDateFormat FORMATTER = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

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
        if (taskPlan == null) {
            return;
        }
        Date startTaskTime = new Date();
        List<BloomFilterTask> loadAllTasks = taskPlan.getOptimizeTasks();
        List<BloomFilterWrapper> bfs = new ArrayList<>(loadAllTasks.size());
        for (BloomFilterTask bftask : loadAllTasks) {
            String bfname = bftask.getBloomFilterName();
            ConfigMeta configMeta = obtainBloomFilterConfigMeta(bfname);
            boolean isLock = obtainBloomFilterLoadLock(bfname, configMeta.getTimeVersion(),
                    bftask.isForceLoadTask());
            if (isLock) {
                BloomFilterTaskResult result = this.bloomFilterLoadTemplate.load(bftask, startTaskTime);
                if (result.isSuccess()) {
                    bfs.add(result.getBloomFilterWrapper());
                } else {
                    this.bloomFilterLoadTemplate.getBloomFilterLockRepository().releaseBloomFilterLock(bfname, new
                            Date());
                    logger.warn("");
                }
            }
        }
        //
        for (BloomFilterWrapper bf : bfs) {
            //放入，and 存储
            boolean isSuccess = this.bloomFilterLoadTemplate.getBloomFilterOutputRepository().storeBloomFilter(bf);
            this.bloomFilterLoadTemplate.getBloomFilterLockRepository().releaseBloomFilterLock("", new
                    Date());
        }
        //logger detail message
        if (logger.isInfoEnabled()) {
            logger.info("");
        }
    }

    /**
     * obtainBloomFilterConfigMeta
     *
     * @param name
     * @return
     */
    private ConfigMeta obtainBloomFilterConfigMeta(String name) {
        BloomFilterConfigRepository bloomFilterConfigRepository = this.bloomFilterLoadTemplate
                .getBloomFilterConfigRepository();
        return bloomFilterConfigRepository.queryConfigMetaByName(name);
    }

    /**
     * obtainBloomFilterLoadLock
     *
     * @param name
     * @param timeVersion
     * @param isForced
     * @return
     */
    private boolean obtainBloomFilterLoadLock(String name, Date timeVersion, boolean isForced) {
        return this.bloomFilterLoadTemplate.getBloomFilterLockRepository().obtainBloomFilterLock(name, timeVersion,
                isForced);
    }

    public void setBloomFilterLoadTemplate(BloomFilterLoadTemplate bloomFilterLoadTemplate) {
        this.bloomFilterLoadTemplate = bloomFilterLoadTemplate;
    }
}
