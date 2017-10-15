package com.mifish.bloomfilter.center.template.build;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.repository.BloomFilterInputRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterTimeVersionRepository;
import com.mifish.bloomfilter.center.template.BloomFilterBuildTemplate;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 21:23
 */
public class SimpleBloomFilterBuildTemplate implements BloomFilterBuildTemplate {

    /***bloomFilterInputRepository*/
    private BloomFilterInputRepository bloomFilterInputRepository;

    /***bloomFilterTimeVersionRepository*/
    private BloomFilterTimeVersionRepository bloomFilterTimeVersionRepository;

    @Override
    public BloomFilterTaskResult build(BloomFilterTask bloomFilterTask) {
        return null;
    }
}
