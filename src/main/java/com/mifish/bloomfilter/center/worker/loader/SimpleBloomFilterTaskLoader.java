package com.mifish.bloomfilter.center.worker.loader;

import com.mifish.bloomfilter.center.container.BloomFilterContainer;
import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskPlan;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.template.BloomFilterLoadTemplate;
import com.mifish.bloomfilter.center.worker.AbstractBloomFilterTaskWorker;
import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    /***bloomFilterContainer*/
    private BloomFilterContainer bloomFilterContainer;

    /***formatter*/
    private static final FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void executeTaskPlan(BloomFilterTaskPlan taskPlan) {
        if (taskPlan == null) {
            return;
        }
        List<BloomFilterTask> loadAllTasks = taskPlan.getOptimizeTasks();
        for (BloomFilterTask bftask : loadAllTasks) {
            this.bloomFilterLoadTemplate.load(bftask);
        }

    }

    public void setBloomFilterLoadTemplate(BloomFilterLoadTemplate bloomFilterLoadTemplate) {
        this.bloomFilterLoadTemplate = bloomFilterLoadTemplate;
    }

    public void setBloomFilterContainer(BloomFilterContainer bloomFilterContainer) {
        this.bloomFilterContainer = bloomFilterContainer;
    }
}
