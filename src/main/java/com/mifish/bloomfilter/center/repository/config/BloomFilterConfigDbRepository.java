package com.mifish.bloomfilter.center.repository.config;

import com.mifish.bloomfilter.center.model.ConfigMeta;
import com.mifish.bloomfilter.center.repository.BloomFilterConfigRepository;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 23:51
 */
public class BloomFilterConfigDbRepository implements BloomFilterConfigRepository {
    
    /**
     * queryConfigMetaByName
     *
     * @param name
     * @return
     */
    @Override
    public ConfigMeta queryConfigMetaByName(String name) {
        return null;
    }
}
