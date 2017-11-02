package com.mifish.bloomfilter.center.repository;

import com.mifish.bloomfilter.center.model.ConfigMeta;

/**
 * Description:
 * <p>
 * 基于timeversion的版本控制
 *
 * @author: rls
 * Date: 2017-11-02 23:35
 */
public interface BloomFilterConfigRepository {

    /**
     * queryConfigMetaByName
     *
     * @param name
     * @return
     */
    ConfigMeta queryConfigMetaByName(String name);
}
