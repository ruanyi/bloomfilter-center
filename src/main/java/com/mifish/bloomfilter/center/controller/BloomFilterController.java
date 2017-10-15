package com.mifish.bloomfilter.center.controller;

/**
 * Description:
 * <p>
 * BloomFilterController
 * <p>
 * <p>
 * User: rls
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


}
