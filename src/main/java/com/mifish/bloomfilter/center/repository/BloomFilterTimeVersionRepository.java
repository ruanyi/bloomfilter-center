package com.mifish.bloomfilter.center.repository;

import java.util.Date;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 20:35
 */
public interface BloomFilterTimeVersionRepository {

    /**
     * queryTimeVersionByName
     *
     * @param name
     * @return
     */
    Date queryTimeVersionByName(String name);
}
