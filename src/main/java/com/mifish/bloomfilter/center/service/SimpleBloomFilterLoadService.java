package com.mifish.bloomfilter.center.service;

import com.mifish.bloomfilter.center.BloomFilterLoadService;
import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.worker.BloomFilterTaskWorker;
import com.mifish.bloomfilter.center.worker.GroupTaskWorkerManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 14:50
 */
public class SimpleBloomFilterLoadService implements BloomFilterLoadService, InitializingBean {

    /***order*/
    private int order = 0;

    /***group*/
    private String group;

    /***bloomFilterName*/
    private String bloomFilterName = "";

    /***groupTaskWorkerManager*/
    private GroupTaskWorkerManager groupTaskWorkerManager;

    /***bloomFilterTaskWorker*/
    private BloomFilterTaskWorker bloomFilterTaskWorker;

    @Override
    public void afterPropertiesSet() throws Exception {
        checkArgument(StringUtils.isNotBlank(this.group), "SimpleBloomFilterLoadService," +
                "group cannot " +
                "be blank");
        checkArgument(StringUtils.isNotBlank(this.bloomFilterName), "SimpleBloomFilterLoadService," +
                "bloomFilterName cannot " +
                "be blank");
        this.bloomFilterTaskWorker = this.groupTaskWorkerManager.report(this.group, this.bloomFilterName, this.order);
        checkArgument(this.bloomFilterTaskWorker != null, "SimpleBloomFilterLoadService,bloomFilterTaskWorker,cannot " +
                "be null");

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
        BloomFilterTask bloomFilterTask = this.groupTaskWorkerManager.allocateTask(this.group, this.bloomFilterName);
        this.bloomFilterTaskWorker.submitTask(bloomFilterTask);
        return false;
    }

    @Override
    public boolean foreLoad() {
        return false;
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
