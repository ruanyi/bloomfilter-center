package com.mifish.bloomfilter.center.model;

import java.io.Serializable;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 20:40
 */
public class QueryIdRange implements Serializable {

    private static final long serialVersionUID = 8230258652962049392L;

    /***min*/
    private long min;

    /***max*/
    private long max;

    /***count*/
    private long count;

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "QueryIdRange{" +
                "min=" + min +
                ", max=" + max +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QueryIdRange that = (QueryIdRange) o;

        if (min != that.min) {
            return false;
        }
        if (max != that.max) {
            return false;
        }
        return count == that.count;
    }

    @Override
    public int hashCode() {
        int result = (int) (min ^ (min >>> 32));
        result = 31 * result + (int) (max ^ (max >>> 32));
        result = 31 * result + (int) (count ^ (count >>> 32));
        return result;
    }
}
