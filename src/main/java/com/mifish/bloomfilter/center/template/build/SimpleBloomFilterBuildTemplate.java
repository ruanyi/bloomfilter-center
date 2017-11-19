package com.mifish.bloomfilter.center.template.build;

import com.mifish.bloomfilter.center.model.BloomFilterKeyData;
import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterWrapper;
import com.mifish.bloomfilter.center.model.ConfigMeta;
import com.mifish.bloomfilter.center.model.QueryIdRange;
import com.mifish.bloomfilter.center.repository.BloomFilterConfigRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterDataRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterInputRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterLockRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterOutputRepository;
import com.mifish.bloomfilter.center.serializer.BloomFilterSerializer;
import com.mifish.bloomfilter.center.serializer.impl.SimpleBloomFilterSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.mifish.bloomfilter.center.util.BloomFilterUtils.formateBloomFilterDate;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 21:23
 */
public class SimpleBloomFilterBuildTemplate extends AbstractBloomFilterBuildTemplate {

    /***LOGGER*/
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleBloomFilterBuildTemplate.class);

    /***bloomFilterInputRepository*/
    private BloomFilterInputRepository bloomFilterInputRepository;

    /***bloomFilterLockRepository*/
    private BloomFilterLockRepository bloomFilterLockRepository;

    /***bloomFilterDataRepository*/
    private BloomFilterDataRepository bloomFilterDataRepository;

    /***bloomFilterConfigRepository*/
    private BloomFilterConfigRepository bloomFilterConfigRepository;

    /***bloomFilterOutputRepository*/
    private BloomFilterOutputRepository bloomFilterOutputRepository;

    /***bloomFilterSerializer*/
    private BloomFilterSerializer bloomFilterSerializer = SimpleBloomFilterSerializer.getInstance();

    /**
     * add2BloomFilter
     *
     * @param buildTask
     * @param startTime
     * @param endTime
     * @param bfwrapper
     * @param idRange
     * @return
     */
    @Override
    protected Date add2BloomFilter(BloomFilterTask buildTask, Date startTime, Date endTime, BloomFilterWrapper
            bfwrapper, QueryIdRange idRange) {
        long beginTime = System.currentTimeMillis();
        String bfname = buildTask.getBloomFilterName();
        String taskId = buildTask.getAttribute("taskId", String.class);
        AtomicLong dataCnt = new AtomicLong(0L);
        int pageSize = this.bloomFilterController.obtainBloomFilterScanPageSize(bfname);
        boolean isTimeOut = false;
        Date ntv = endTime;
        Date lastModifyTime = null;
        long startId, endId, max;
        for (startId = idRange.getMin(), max = idRange.getMax(); startId <= max; ) {
            endId = startId + pageSize;
            List<BloomFilterKeyData> bfdatas = this.bloomFilterDataRepository.queryKeysByPage(startTime, endTime,
                    startId, endId);
            if (bfdatas == null) {
                throw new RuntimeException("");
            }
            for (BloomFilterKeyData keyData : bfdatas) {
                bfwrapper.add(keyData.getKey());
                dataCnt.incrementAndGet();
            }
            if (isBuildTimeOut(beginTime, bfname)) {
                isTimeOut = true;
                LOGGER.warn("");
                break;
            }
            startId = endId;
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("SimpleBloomFilterBuildTemplate,add2BloomFilter,bfname:" + bfname + ",taskId:" + taskId + "," +
                    "startTime:" + formateBloomFilterDate(startTime) +
                    ",endTime:" + formateBloomFilterDate(endTime) + "," + idRange + ",size of bf:" + dataCnt
                    .longValue() + ",isTimeOut:" + isTimeOut + ",cost:" + (System.currentTimeMillis() - beginTime));
        }
        return ntv;
    }

    /**
     * 判断构建是否已经超时
     *
     * @param beginTime
     * @param bfname
     * @return
     */
    private boolean isBuildTimeOut(long beginTime, String bfname) {
        int configTimeOut = this.bloomFilterController.obtainBloomFilterBuildTimeOut(bfname);
        if (configTimeOut <= 0) {
            return false;
        }
        return (System.currentTimeMillis() - beginTime) >= TimeUnit.SECONDS.toMillis(configTimeOut);
    }

    /**
     * queryBloomFilterDataRange
     *
     * @param buildTask
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    protected QueryIdRange queryBloomFilterDataRange(BloomFilterTask buildTask, Date startTime, Date endTime) {
        String bfname = buildTask.getBloomFilterName();
        String taskId = buildTask.getAttribute("taskId", String.class);
        QueryIdRange idRange = this.bloomFilterDataRepository.queryIdRange(startTime, endTime);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("SimpleBloomFilterBuildTemplate,queryBloomFilterDataRange,bfname:" + bfname + ",taskId:" +
                    taskId + "startTime:" + formateBloomFilterDate(startTime) + ",endTime:" + formateBloomFilterDate
                    (endTime) + ",isSuccess:" + (idRange != null) + "," + idRange);
        }
        return idRange;
    }

    /**
     * obtainBloomFilterWrapper
     *
     * @param buildTask
     * @return
     */
    @Override
    protected BloomFilterWrapper obtainBloomFilterWrapper(BloomFilterTask buildTask) {
        String bfname = buildTask.getBloomFilterName();
        String taskId = buildTask.getAttribute("taskId", String.class);
        ConfigMeta configMeta = buildTask.getAttribute("config_meta", ConfigMeta.class);
        byte[] bfdatas = this.bloomFilterInputRepository.obtainBloomFilterByUrl(configMeta.getUrl());
        BloomFilterWrapper bfwrapper = this.bloomFilterSerializer.deserialize(bfdatas);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("SimpleBloomFilterBuildTemplate,obtainBloomFilterWrapper,bfname:" + bfname + ",taskId:" +
                    taskId + ",Y");
        }
        return bfwrapper;
    }

    /**
     * getBloomFilterLockRepository
     *
     * @return
     */
    @Override
    public BloomFilterLockRepository getBloomFilterLockRepository() {
        return this.bloomFilterLockRepository;
    }

    /**
     * getBloomFilterConfigRepository
     *
     * @return
     */
    @Override
    public BloomFilterConfigRepository getBloomFilterConfigRepository() {
        return this.bloomFilterConfigRepository;
    }

    /**
     * getBloomFilterOutputRepository
     *
     * @return
     */
    @Override
    public BloomFilterOutputRepository getBloomFilterOutputRepository() {
        return this.bloomFilterOutputRepository;
    }

    /**
     * setBloomFilterInputRepository
     *
     * @param bloomFilterInputRepository
     */
    public void setBloomFilterInputRepository(BloomFilterInputRepository bloomFilterInputRepository) {
        this.bloomFilterInputRepository = bloomFilterInputRepository;
    }

    /**
     * setBloomFilterLockRepository
     *
     * @param bloomFilterLockRepository
     */
    public void setBloomFilterLockRepository(BloomFilterLockRepository bloomFilterLockRepository) {
        this.bloomFilterLockRepository = bloomFilterLockRepository;
    }

    /**
     * setBloomFilterDataRepository
     *
     * @param bloomFilterDataRepository
     */
    public void setBloomFilterDataRepository(BloomFilterDataRepository bloomFilterDataRepository) {
        this.bloomFilterDataRepository = bloomFilterDataRepository;
    }

    /**
     * setBloomFilterConfigRepository
     *
     * @param bloomFilterConfigRepository
     */
    public void setBloomFilterConfigRepository(BloomFilterConfigRepository bloomFilterConfigRepository) {
        this.bloomFilterConfigRepository = bloomFilterConfigRepository;
    }

    /**
     * setBloomFilterOutputRepository
     *
     * @param bloomFilterOutputRepository
     */
    public void setBloomFilterOutputRepository(BloomFilterOutputRepository bloomFilterOutputRepository) {
        this.bloomFilterOutputRepository = bloomFilterOutputRepository;
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
