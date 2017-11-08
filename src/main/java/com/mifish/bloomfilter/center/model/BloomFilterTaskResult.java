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
     * retCode
     */
    private int retCode;

    /**
     * message
     */
    private String message;

    /**
     * bloomFilterWrapper
     */
    private BloomFilterWrapper bloomFilterWrapper;

    /**
     * BloomFilterTaskResult
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BloomFilterTaskResult result = (BloomFilterTaskResult) o;

        if (retCode != result.retCode) {
            return false;
        }
        if (message != null ? !message.equals(result.message) : result.message != null) {
            return false;
        }
        return bloomFilterWrapper != null ? bloomFilterWrapper.equals(result.bloomFilterWrapper) : result
                .bloomFilterWrapper == null;
    }

    @Override
    public int hashCode() {
        int result = retCode;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (bloomFilterWrapper != null ? bloomFilterWrapper.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BloomFilterTaskResult{" +
                "retCode=" + retCode +
                ", message='" + message + '\'' +
                '}';
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
