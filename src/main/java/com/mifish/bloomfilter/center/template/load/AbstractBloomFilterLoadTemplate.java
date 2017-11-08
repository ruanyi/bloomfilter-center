package com.mifish.bloomfilter.center.template.load;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.model.BloomFilterWrapper;
import com.mifish.bloomfilter.center.model.ConfigMeta;
import com.mifish.bloomfilter.center.model.Pair;
import com.mifish.bloomfilter.center.template.BloomFilterLoadTemplate;

import java.util.Optional;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-05 20:27
 */
public abstract class AbstractBloomFilterLoadTemplate implements BloomFilterLoadTemplate {

    /**
     * load
     *
     * @param loadTask
     * @return
     */
    @Override
    public BloomFilterTaskResult load(BloomFilterTask loadTask) {
        if (loadTask == null || !loadTask.isLoadTask()) {
            return BloomFilterTaskResult.FAILURE(-1, "loadTask is illegal");
        }
        String bfname = loadTask.getBloomFilterName();
        String taskId = loadTask.getAttribute("taskId", String.class);
        ConfigMeta configMeta = loadTask.getAttribute("config_meta", ConfigMeta.class);
        Optional<Pair<Long, BloomFilterWrapper>> op = Optional.empty();
        //假如不是强制加载，则先从本地加载，否则，直接从远端加载最新版本
        if (!loadTask.isForceLoadTask()) {
            op = loadAndSerializeLocalBloomFilter(taskId, configMeta);
        }
        //假如是强制加载任务，或者：本地从来没有加载过，则从远端加载最新版本
        if (!op.isPresent()) {
            op = loadAndSerializeRemoteBloomFilter(taskId, configMeta);
        }
        //假如指定的版本，还是加载不到，则从本地加载最新的版本，目的：损失业务精度为代码，换取系统的稳定性
        if (!op.isPresent()) {
            op = loadAndSerializeLastBloomFilter(taskId, configMeta);
        }
        if (op.isPresent()) {
            //以configMeta为主
            long bloomfilterFileSize = op.get().getFirst();
            BloomFilterWrapper blwrapper = op.get().getSecond();
            blwrapper.setBloomfilterFileSize(bloomfilterFileSize);
            blwrapper.setTimeVersion(configMeta.getTimeVersion());
            return BloomFilterTaskResult.SUCCESS(op.get().getSecond());
        }
        //
        return BloomFilterTaskResult.FAILURE(1, "load bloomfilter " + bfname + " failure");
    }

    /**
     * loadAndSerializeLastBloomFilter
     *
     * @param taskId
     * @param configMeta
     * @return
     */
    protected abstract Optional<Pair<Long, BloomFilterWrapper>> loadAndSerializeLastBloomFilter(String taskId,
                                                                                                ConfigMeta configMeta);

    /**
     * loadAndSerializeRemoteBloomFilter
     *
     * @param taskId
     * @param configMeta
     * @return
     */
    protected abstract Optional<Pair<Long, BloomFilterWrapper>> loadAndSerializeRemoteBloomFilter(String taskId,
                                                                                                  ConfigMeta
                                                                                                          configMeta);

    /**
     * loadAndSerializeLocalBloomFilter
     *
     * @param taskId
     * @param configMeta
     * @return
     */
    protected abstract Optional<Pair<Long, BloomFilterWrapper>> loadAndSerializeLocalBloomFilter(String taskId,
                                                                                                 ConfigMeta configMeta);
}
