package com.mifish.bloomfilter.center.repository;

import com.mifish.bloomfilter.center.model.BloomFilterKeyData;
import com.mifish.bloomfilter.center.model.QueryIdRange;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 20:39
 */
public interface BloomFilterDataRepository {

    /**
     * queryAllDataCount
     *
     * @param startTime
     * @param endTime
     * @return
     */
    long queryAllDataCount(Date startTime, Date endTime);

    /**
     * queryIdRange
     *
     * @param startTime
     * @param endTime
     * @return
     */
    QueryIdRange queryIdRange(Date startTime, Date endTime);

    /**
     * queryKeysByPage
     *
     * @param startTime
     * @param endTime
     * @param startId
     * @param endId
     * @return
     */
    List<BloomFilterKeyData> queryKeysByPage(Date startTime, Date endTime, long startId, long endId);
}
