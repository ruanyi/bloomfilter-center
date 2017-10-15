package com.mifish.bloomfilter.center;

/**
 * Description:
 * <p>
 *
 * @author : rls
 * Date: 2017-10-13 23:31
 */
public interface BloomFilterLoadService {

    /**
     * order
     * <p>
     * 范围：从0开始到整形最大值，越小代表优先级越高
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
     * loader bloomfilter
     *
     * @return
     */
    boolean load();

    /**
     * fore loader bloomfilter
     *
     * @return
     */
    boolean foreLoad();
}
