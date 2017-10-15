package com.mifish.bloomfilter.center.template.build;

import com.mifish.bloomfilter.center.controller.BloomFilterController;
import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.template.BloomFilterBuildTemplate;

import java.util.Date;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 21:25
 */
public abstract class AbstractBloomFilterBuildTemplate implements BloomFilterBuildTemplate {

    /***bloomFilterController*/
    protected BloomFilterController bloomFilterController;

    /**
     * build
     *
     * @param bloomFilterTask
     * @return
     */
    @Override
    public BloomFilterTaskResult build(BloomFilterTask bloomFilterTask, Date startTaskTime) {

        return null;
    }

    public void setBloomFilterController(BloomFilterController bloomFilterController) {
        this.bloomFilterController = bloomFilterController;
    }
}
