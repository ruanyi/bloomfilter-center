package com.mifish.bloomfilter.center.serializer;

import com.mifish.bloomfilter.center.model.BloomFilterWrapper;

/**
 * Description:
 * <p>
 * User: rls
 * Date: 2017-10-14 00:09
 */
public interface BloomFilterSerializer {

    /**
     * serializer
     *
     * @param wrapper
     * @return
     */
    byte[] serialize(BloomFilterWrapper wrapper);

    /**
     * deserialize
     *
     * @param datas
     * @return
     */
    BloomFilterWrapper deserialize(byte[] datas);
}
