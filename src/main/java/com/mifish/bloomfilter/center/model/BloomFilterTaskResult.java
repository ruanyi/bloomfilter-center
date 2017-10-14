package com.mifish.bloomfilter.center.model;

import java.io.Serializable;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-14 11:54
 */
public class BloomFilterTaskResult implements Serializable {

    /**
     * bloomFilterTask
     */
    private BloomFilterTask bloomFilterTask;

    /**
     * rscode
     */
    private int rscode;

    /**
     * message
     */
    private String message;

    /**
     * bloomFilterWrapper
     */
    private BloomFilterWrapper bloomFilterWrapper;

    public BloomFilterTask getBloomFilterTask() {
        return bloomFilterTask;
    }

    public void setBloomFilterTask(BloomFilterTask bloomFilterTask) {
        this.bloomFilterTask = bloomFilterTask;
    }

    public int getRscode() {
        return rscode;
    }

    public void setRscode(int rscode) {
        this.rscode = rscode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BloomFilterWrapper getBloomFilterWrapper() {
        return bloomFilterWrapper;
    }

    public void setBloomFilterWrapper(BloomFilterWrapper bloomFilterWrapper) {
        this.bloomFilterWrapper = bloomFilterWrapper;
    }
}
