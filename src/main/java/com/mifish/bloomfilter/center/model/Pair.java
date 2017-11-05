package com.mifish.bloomfilter.center.model;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-05 20:46
 */
public class Pair<FIRST, SECOND> {

    /***first*/
    private FIRST first;

    /***second*/
    private SECOND second;

    public Pair() {

    }

    /**
     * Pair
     *
     * @param first
     * @param second
     */
    public Pair(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }

    /**
     * getFirst
     *
     * @return
     */
    public FIRST getFirst() {
        return first;
    }

    /**
     * setFirst
     *
     * @param first
     */
    public void setFirst(FIRST first) {
        this.first = first;
    }

    /**
     * getSecond
     *
     * @return
     */
    public SECOND getSecond() {
        return second;
    }

    /**
     * setSecond
     *
     * @param second
     */
    public void setSecond(SECOND second) {
        this.second = second;
    }

    /**
     * of
     *
     * @param first
     * @param second
     * @param <F>
     * @param <S>
     * @return
     */
    public static final <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }
}
