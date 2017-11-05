package com.mifish.bloomfilter.center;

/**
 * Description:
 * <p>
 * User: rls
 * Date: 2017-10-13 21:05
 */
public final class BloomFilterConstant {

    public static final String DEFAULT_CHARSET_GBK = "gbk";

    public static final int DEFAULT_WORK_QUEUE_NUM = 200;

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
