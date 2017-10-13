package com.mifish.bloomfilter.center;

/**
 * Description:
 * <p>
 * User: rls
 * Date: 2017-10-13 23:35
 */
public interface BloomFilterBuilder {

    /**
     * order
     *
     * @return
     */
    int order();

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
