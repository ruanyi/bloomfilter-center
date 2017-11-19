package com.mifish.bloomfilter.center.controller;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.mifish.bloomfilter.center.BloomFilterConstant;

import java.util.Map;

import static com.mifish.bloomfilter.center.BloomFilterConstant.DEFAULT_CHARSET_GBK;
import static com.mifish.bloomfilter.center.BloomFilterConstant.DEFAULT_POSITIVE_PROBABILITY;
import static com.mifish.bloomfilter.center.BloomFilterConstant.DEFAULT_SCAN_PAGE_SIZE;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-19 18:45
 */
public class AbstractBloomFilterController implements BloomFilterController {

    /**
     * 基于google的事件总线，
     * 用于：手工控制布隆索引的加载、构建等等
     */
    protected EventBus eventBus;

    /**
     * 配置布隆索引是否生效：
     * 假如配置成：NONE：则所有都不生效
     * ALL：默认所有都生效
     * 名字1,名字2：则部分生效
     * <p>
     * 默认是：不生效
     */
    protected Map<String, Boolean> bloomFilterValids = Maps.newHashMap();

    /**
     * 配置布隆索引的误差率
     * <p>
     * 假如没有配置，默认是：0.004
     */
    protected Map<String, Double> bloomFilterPositiveProbabilities = Maps.newHashMap();

    /**
     * 配置的布隆索引构建的字符串编码
     * 假如没有配置，则是：GBK
     */
    protected Map<String, String> bloomFilterCharsets = Maps.newHashMap();

    /**
     * 配置布隆索引构建时的并发数
     * 假如没有配置，则默认没有并发，串行
     */
    protected Map<String, Integer> bloomFilterBuildPoolSizes = Maps.newHashMap();

    /**
     * 配置布隆索引构建时，每次扫描的数据量
     * 假如没有配置，则默认1000
     */
    protected Map<String, Integer> bloomFilterScanPageSizes = Maps.newHashMap();

    /**
     * 配置布隆索引构建时，初始化大小的值
     * <p>
     * 假如没有配置，则是-1
     */
    protected Map<String, Long> bloomFilterBuildInitCounts = Maps.newHashMap();

    /**
     * 配置布隆索引构建时，超时时间设置
     * 假如没有配置：默认-1，不超时，继续构建
     */
    protected Map<String, Integer> bloomFilterBuildTimeOuts = Maps.newHashMap();


    /**
     * isBloomFilterValid
     *
     * @param bfname
     * @return
     */
    @Override
    public boolean isBloomFilterValid(String bfname) {
        if (this.bloomFilterValids.containsKey(BloomFilterConstant.NONE)) {
            return false;
        }
        if (this.bloomFilterValids.containsKey(BloomFilterConstant.ALL)) {
            return true;
        }
        if (this.bloomFilterValids.containsKey(bfname)) {
            return this.bloomFilterValids.get(bfname);
        }
        return false;
    }

    /**
     * obtainBloomFilterPositiveProbability
     *
     * @param bfname
     * @return
     */
    @Override
    public double obtainBloomFilterPositiveProbability(String bfname) {
        if (this.bloomFilterPositiveProbabilities.containsKey(bfname)) {
            return this.bloomFilterPositiveProbabilities.get(bfname);
        }
        return DEFAULT_POSITIVE_PROBABILITY;
    }

    /**
     * obtainBloomFilterCharset
     *
     * @param bfname
     * @return
     */
    @Override
    public String obtainBloomFilterCharset(String bfname) {
        if (this.bloomFilterCharsets.containsKey(bfname)) {
            return this.bloomFilterCharsets.get(bfname);
        }
        return DEFAULT_CHARSET_GBK;
    }

    /**
     * obtainBloomFilterBuildPoolSize
     *
     * @param bfname
     * @return
     */
    @Override
    public int obtainBloomFilterBuildPoolSize(String bfname) {
        if (this.bloomFilterBuildPoolSizes.containsKey(bfname)) {
            return this.bloomFilterBuildPoolSizes.get(bfname);
        }
        return -1;
    }

    /**
     * obtainBloomFilterBuildInitCount
     *
     * @param bfname
     * @return
     */
    @Override
    public long obtainBloomFilterBuildInitCount(String bfname) {
        if (this.bloomFilterBuildInitCounts.containsKey(bfname)) {
            return this.bloomFilterBuildInitCounts.get(bfname);
        }
        return -1;
    }

    /**
     * obtainBloomFilterScanPageSize
     *
     * @param bfname
     * @return
     */
    @Override
    public int obtainBloomFilterScanPageSize(String bfname) {
        if (this.bloomFilterScanPageSizes.containsKey(bfname)) {
            return this.bloomFilterScanPageSizes.get(bfname);
        }
        return DEFAULT_SCAN_PAGE_SIZE;
    }

    /**
     * obtainBloomFilterBuildTimeOut
     *
     * @param bfname
     * @return
     */
    @Override
    public int obtainBloomFilterBuildTimeOut(String bfname) {
        if (this.bloomFilterBuildTimeOuts.containsKey(bfname)) {
            return this.bloomFilterBuildTimeOuts.get(bfname);
        }
        return -1;
    }
}
