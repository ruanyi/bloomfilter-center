package com.mifish.bloomfilter.center.model;

import java.io.Serializable;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-14 11:54
 */
public class BloomFilterTaskResult implements Serializable {

    /***retCode*/
    private int retCode;

    /***message*/
    private String message;

    /***bloomFilterWrapper*/
    private BloomFilterWrapper bloomFilterWrapper;

    /***/
    private BloomFilterTaskResult() {

    }

    /**
     * isSuccess
     *
     * @return
     */
    public boolean isSuccess() {
        return this.retCode == 0;
    }

    /**
     * getRetCode
     *
     * @return
     */
    public int getRetCode() {
        return retCode;
    }

    /**
     * getMessage
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * getBloomFilterWrapper
     *
     * @return
     */
    public BloomFilterWrapper getBloomFilterWrapper() {
        return bloomFilterWrapper;
    }

    /**
     * SUCCESS
     *
     * @param bloomFilterWrapper
     * @return
     */
    public static BloomFilterTaskResult SUCCESS(BloomFilterWrapper bloomFilterWrapper) {
        BloomFilterTaskResult result = new BloomFilterTaskResult();
        result.retCode = 0;
        result.message = "success";
        result.bloomFilterWrapper = bloomFilterWrapper;
        return result;
    }

    /**
     * FAILUER
     *
     * @param retCode
     * @param message
     * @return
     */
    public static BloomFilterTaskResult FAILURE(int retCode, String message) {
        if (retCode == 0) {
            throw new IllegalArgumentException("BloomFilterTaskResult,retCode cannot be 0");
        }
        BloomFilterTaskResult result = new BloomFilterTaskResult();
        result.retCode = retCode;
        result.message = message;
        return result;
    }

    /**
     * FAILUER
     *
     * @param retCode
     * @return
     */
    public static BloomFilterTaskResult FAILURE(int retCode) {
        return FAILURE(retCode, "failure");
    }
}
