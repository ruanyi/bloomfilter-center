package com.mifish.bloomfilter.center.util;

/**
 * Description:
 * <p>
 * m : bits数组的长度
 * n : 需要加入其中的key的数量
 * k : k次hash函数
 * f : 误差率(false positive)
 * <p>
 * <p>
 *
 * @author : rls
 * Date: 2017-10-13 21:28
 */
public final class BloomFilterUtils {

    /***BloomFilterUtils*/
    private BloomFilterUtils() {

    }


    /**
     * caculateOptimalNumOfHashFunctions
     *
     * @param n
     * @param m
     * @return
     */
    public static int caculateOptimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / (double) n * Math.log(2.0D)));
    }

    /**
     * caculateOptimalNumOfBits
     *
     * @param n
     * @param f
     * @return
     */
    public static long caculateOptimalNumOfBits(long n, double f) {
        if (f == 0.0D) {
            f = 4.9E-324D;
        }
        return (long) ((double) (-n) * Math.log(f) / (Math.log(2.0D) * Math.log(2.0D)));
    }

    /**
     * caculateFalsePositiveProbability
     *
     * @param n
     * @param m
     * @param k
     * @return
     */
    public static double caculateFalsePositiveProbability(long n, long m, int k) {
        return Math.pow((double) (m / n), k);
    }

    /**
     * caculateNumberAddElements
     *
     * @param m
     * @param f
     * @return
     */
    public static double caculateNumberAddElements(long m, double f) {
        return (m * Math.log(0.6185f) / Math.log(f));
    }
}