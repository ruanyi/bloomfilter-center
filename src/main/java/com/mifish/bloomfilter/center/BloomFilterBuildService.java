package com.mifish.bloomfilter.center;

/**
 * Description:
 * <p>
 *
 * @author :rls
 * Date: 2017-10-13 23:35
 */
public interface BloomFilterBuildService {

    /**
     * order
     *
     * @return
     */
    int order();

    /**
     * group
     *
     * @return
     */
    String group();

    /**
     * getBloomFilterName
     *
     * @return
     */
    String getBloomFilterName();

    /**
     * build bloomfilter
     *
     * @return
     */
    boolean build();

    /**
     * fore build bloomfilter
     *
     * @return
     */
    boolean foreBuild();

}
