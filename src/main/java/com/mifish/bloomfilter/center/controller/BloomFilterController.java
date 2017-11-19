package com.mifish.bloomfilter.center.controller;

/**
 * Description:
 * <p>
 *
 * @author rls
 * Date: 2017-10-14 00:14
 */
public interface BloomFilterController {

    /**
     * 判断某一个布隆索引是否生效
     *
     * @param bfname
     * @return
     */
    boolean isBloomFilterValid(String bfname);

    /**
     * obtainBloomFilterPositiveProbability
     *
     * @param bfname
     * @return
     */
    double obtainBloomFilterPositiveProbability(String bfname);

    /**
     * obtainBloomFilterCharset
     *
     * @param bfname
     * @return
     */
    String obtainBloomFilterCharset(String bfname);

    /**
     * obtainBloomFilterBuildPoolSize
     *
     * @param bfname
     * @return
     */
    int obtainBloomFilterBuildPoolSize(String bfname);

    /**
     * obtainBloomFilterBuildInitCount
     *
     * @param bfname
     * @return
     */
    long obtainBloomFilterBuildInitCount(String bfname);

    /**
     * obtainBloomFilterScanPageSize
     *
     * @param bfname
     * @return
     */
    int obtainBloomFilterScanPageSize(String bfname);

    /**
     * obtainBloomFilterBuildTimeOut
     *
     * @param bfname
     * @return
     */
    int obtainBloomFilterBuildTimeOut(String bfname);

}
