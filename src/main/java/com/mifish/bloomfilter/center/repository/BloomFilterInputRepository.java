package com.mifish.bloomfilter.center.repository;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 20:38
 */
public interface BloomFilterInputRepository {

    /**
     * obtainBloomFilterByUrl
     *
     * @param url
     * @return
     */
    byte[] obtainBloomFilterByUrl(String url);
}
