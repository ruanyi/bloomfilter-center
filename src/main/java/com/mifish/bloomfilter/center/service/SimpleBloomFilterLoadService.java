package com.mifish.bloomfilter.center.service;

import com.mifish.bloomfilter.center.BloomFilterLoadService;
import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.worker.BloomFilterTaskWorker;
import com.mifish.bloomfilter.center.worker.GroupTaskWorkerManager;
import com.mifish.bloomfilter.center.worker.TaskWorkerType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 14:50
 */
public class SimpleBloomFilterLoadService implements BloomFilterLoadService, InitializingBean {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(SimpleBloomFilterLoadService.class);

    /***order*/
    private int order = 0;

    /***group*/
    private String group;

    /***bloomFilterName*/
    private String bloomFilterName = "";

    /***groupTaskWorkerManager*/
    private GroupTaskWorkerManager groupTaskWorkerManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        checkArgument(StringUtils.isNotBlank(this.group), "SimpleBloomFilterLoadService," +
                "group cannot " +
                "be blank");
        checkArgument(StringUtils.isNotBlank(this.bloomFilterName), "SimpleBloomFilterLoadService," +
                "bloomFilterName cannot " +
                "be blank");
        this.groupTaskWorkerManager.report(TaskWorkerType.LOADER, this.group, this
                .bloomFilterName, this.order);
        /**触发一次启动时加载的任务*/
        BloomFilterTask loadTask = BloomFilterTask.buildStartUpLoadTask(this.group, this.bloomFilterName, this.order);
        boolean isSuccess = this.groupTaskWorkerManager.allocate(TaskWorkerType.LOADER, this.group).submitTask
                (loadTask);
        if (isSuccess) {
            if (logger.isDebugEnabled()) {
                logger.debug("SimpleBloomFilterLoadService,afterPropertiesSet,bloomfilter[" + this.bloomFilterName +
                        "],submit BloomFilterTask[" + loadTask + "],isSuccess[" + isSuccess + "]");
            }
        } else {
            logger.error("SimpleBloomFilterLoadService,afterPropertiesSet,bloomfilter[" + this.bloomFilterName +
                    "],submit BloomFilterTask[" + loadTask + "],isSuccess[" + isSuccess + "],wait next load task");
        }
    }

    @Override
    public int order() {
        return this.order;
    }

    @Override
    public String group() {
        return this.group;
    }

    @Override
    public String getBloomFilterName() {
        return this.bloomFilterName;
    }

    @Override
    public boolean load() {
        BloomFilterTask loadTask = BloomFilterTask.buildNormalLoadTask(this.group, this.bloomFilterName, this.order);
        boolean isSuccess = this.groupTaskWorkerManager.allocate(TaskWorkerType.LOADER, this.group).submitTask
                (loadTask);
        if (isSuccess) {
            if (logger.isDebugEnabled()) {
                logger.debug("SimpleBloomFilterLoadService,load[" + loadTask + "],isSuccess[" + isSuccess + "]");
            }
        } else {
            logger.error("SimpleBloomFilterLoadService,load[" + loadTask + "],isSuccess[" + isSuccess + "]");
        }
        return isSuccess;
    }

    @Override
    public boolean foreLoad() {
        BloomFilterTask loadTask = BloomFilterTask.buildForceLoadTask(this.group, this.bloomFilterName, this.order);
        boolean isSuccess = this.groupTaskWorkerManager.allocate(TaskWorkerType.LOADER, this.group).submitTask
                (loadTask);
        if (isSuccess) {
            if (logger.isDebugEnabled()) {
                logger.debug("SimpleBloomFilterLoadService,foreLoad[" + loadTask + "],isSuccess[" + isSuccess + "]");
            }
        } else {
            logger.error("SimpleBloomFilterLoadService,foreLoad[" + loadTask + "],isSuccess[" + isSuccess + "]");
        }
        return isSuccess;
    }

    /**
     * setOrder
     *
     * @param order
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * setGroup
     *
     * @param group
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * setBloomFilterName
     *
     * @param bloomFilterName
     */
    public void setBloomFilterName(String bloomFilterName) {
        this.bloomFilterName = bloomFilterName;
    }

    /**
     * setGroupTaskWorkerManager
     *
     * @param groupTaskWorkerManager
     */
    public void setGroupTaskWorkerManager(GroupTaskWorkerManager groupTaskWorkerManager) {
        this.groupTaskWorkerManager = groupTaskWorkerManager;
    }
}
