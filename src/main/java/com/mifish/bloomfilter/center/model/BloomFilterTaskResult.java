package com.mifish.bloomfilter.center.model;

import java.io.Serializable;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-14 11:54
 */
public class BloomFilterTaskResult implements Serializable {

    /***bloomFilterTask */
    private BloomFilterTask bftask;

    /***retCode*/
    private int retCode;

    /***message*/
    private String message;

    /***bloomFilterWrapper*/
    private BloomFilterWrapper bloomFilterWrapper;

    public BloomFilterTask getBftask() {
        return bftask;
    }

    public void setBftask(BloomFilterTask bftask) {
        this.bftask = bftask;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
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
