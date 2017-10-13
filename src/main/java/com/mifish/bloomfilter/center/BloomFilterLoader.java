package com.mifish.bloomfilter.center;

/**
 * Description:
 * <p>
 * User: rls
 * Date: 2017-10-13 23:31
 */
public interface BloomFilterLoader {

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
     * load bloomfilter
     *
     * @return
     */
    boolean load();

    /**
     * fore load bloomfilter
     *
     * @return
     */
    boolean foreLoad();
}
