package com.mifish.bloomfilter.center.service;

import com.mifish.bloomfilter.center.BloomFilterBuildService;
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
 * Date: 2017-10-15 17:43
 */
public class SimpleBloomFilterBuildService implements BloomFilterBuildService, InitializingBean {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(SimpleBloomFilterBuildService.class);

    /***order*/
    private int order = 0;

    /***group*/
    private String group;

    /***bloomFilterName*/
    private String bloomFilterName = "";

    /***groupTaskWorkerManager*/
    private GroupTaskWorkerManager groupTaskWorkerManager;

    /**
     * afterPropertiesSet
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        checkArgument(StringUtils.isNotBlank(this.group), "SimpleBloomFilterBuildService," +
                "group cannot " +
                "be blank");
        checkArgument(StringUtils.isNotBlank(this.bloomFilterName), "SimpleBloomFilterBuildService," +
                "bloomFilterName cannot " +
                "be blank");
        this.groupTaskWorkerManager.report(TaskWorkerType.BUILDER, this.group, this
                .bloomFilterName, this.order);
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
    public boolean build() {
        BloomFilterTask buildTask = BloomFilterTask.buildNormalBuildTask(this.group, this.bloomFilterName, this.order);
        boolean isSuccess = this.groupTaskWorkerManager.allocate(TaskWorkerType.BUILDER, this.group).submitTask
                (buildTask);
        if (isSuccess) {
            if (logger.isDebugEnabled()) {
                logger.debug("SimpleBloomFilterBuildService,build[" + buildTask + "],isSuccess[" + isSuccess + "]");
            }
        } else {
            logger.error("SimpleBloomFilterBuildService,build[" + buildTask + "],isSuccess[" + isSuccess + "]");
        }
        return isSuccess;
    }

    @Override
    public boolean foreBuild() {
        BloomFilterTask buildTask = BloomFilterTask.buildForceLoadTask(this.group, this.bloomFilterName, this.order);
        boolean isSuccess = this.groupTaskWorkerManager.allocate(TaskWorkerType.BUILDER, this.group).submitTask
                (buildTask);
        if (isSuccess) {
            if (logger.isDebugEnabled()) {
                logger.debug("SimpleBloomFilterBuildService,foreBuild[" + buildTask + "],isSuccess[" + isSuccess + "]");
            }
        } else {
            logger.error("SimpleBloomFilterBuildService,foreBuild[" + buildTask + "],isSuccess[" + isSuccess + "]");
        }
        return isSuccess;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setBloomFilterName(String bloomFilterName) {
        this.bloomFilterName = bloomFilterName;
    }

    public void setGroupTaskWorkerManager(GroupTaskWorkerManager groupTaskWorkerManager) {
        this.groupTaskWorkerManager = groupTaskWorkerManager;
    }
}
