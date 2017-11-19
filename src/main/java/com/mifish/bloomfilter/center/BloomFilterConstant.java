package com.mifish.bloomfilter.center;

/**
 * 关于布隆索引的一些常量工具类
 *
 * @author : rls
 * Date: 2017-10-13 21:05
 */
public final class BloomFilterConstant {

    public static final String DEFAULT_CHARSET_GBK = "gbk";

    public static final int DEFAULT_WORK_QUEUE_NUM = 200;

    public static final double DEFAULT_POSITIVE_PROBABILITY = 0.004d;

    public static final int DEFAULT_SCAN_PAGE_SIZE = 1000;

    public static final String NONE = "none";

    public static final String ALL = "all";

    /***BIT_FACTOR*/
    public static final int BIT_FACTOR = 8;

    /***K_FACTOR*/
    public static final double K_FACTOR = 1024D;

    /***M_FACTOR*/
    public static final double M_FACTOR = K_FACTOR * K_FACTOR;

    private BloomFilterConstant() {
        throw new RuntimeException("cannot instance BloomFilterConstant");
    }
}
