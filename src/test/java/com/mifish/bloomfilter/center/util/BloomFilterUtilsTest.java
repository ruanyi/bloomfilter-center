package com.mifish.bloomfilter.center.util;

import org.junit.Test;

/**
 * Description:
 * <p>
 * @author : rls
 * Date: 2017-10-13 22:54
 */
public class BloomFilterUtilsTest {

    @Test
    public void testCaculateNumberAddElements() {
        System.out.println(BloomFilterUtils.caculateNumberAddElements(Integer.MAX_VALUE, 0.004f));
    }

    @Test
    public void testCaculateOptimalNumOfHashFunctions() {
        System.out.println(BloomFilterUtils.caculateFalsePositiveProbability(10000, Integer.MAX_VALUE, 10));
    }
}
