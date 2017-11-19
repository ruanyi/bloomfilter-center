package com.mifish.bloomfilter.center.strategy;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-19 16:37
 */
public class SimpleTaskOptimizeStrategy extends AbstractTaskOptimizeStrategy {


    public static TaskOptimizeStrategy getInstance() {
        return SimpleTaskOptimizeStrategyHolder.INSTANCE;
    }

    private static class SimpleTaskOptimizeStrategyHolder {
        private static SimpleTaskOptimizeStrategy INSTANCE = new SimpleTaskOptimizeStrategy();
    }

}
