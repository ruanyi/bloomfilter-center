package com.mifish.bloomfilter.center.trigger.schedule;

import com.google.common.eventbus.Subscribe;
import com.mifish.bloomfilter.center.model.BloomFilterCommand;
import com.mifish.bloomfilter.center.trigger.AbstractBloomFilterLoadTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 12:35
 */
public class ScheduleBloomFilterLoadTrigger extends AbstractBloomFilterLoadTrigger implements InitializingBean {

    /***ScheduleBloomFilterLoadTrigger*/
    private static final Logger logger = LoggerFactory.getLogger(ScheduleBloomFilterLoadTrigger.class);

    /***scheduledExectors*/
    private ScheduledExecutorService scheduledExectors = Executors.newSingleThreadScheduledExecutor();

    /***initialDelay*/
    private int initialDelay = 150;

    /***delay*/
    private int delay = 30;

    @Override
    public void afterPropertiesSet() throws Exception {
        scheduleLoadBloomFilter(initialDelay, delay);
    }

    /**
     * scheduleLoadBloomFilter
     *
     * @param initialDelay
     * @param delay
     */
    private void scheduleLoadBloomFilter(final long initialDelay, final int delay) {
        //随机散列化加载，防止集群并发度太高
        long initDelay = initialDelay + Math.round(Math.random() * 100 / 3);
        this.scheduledExectors.scheduleWithFixedDelay(() -> triggerLoad(false), initDelay, delay, TimeUnit.SECONDS);
        //logger detail msg
        if (logger.isInfoEnabled()) {
            logger.info("ScheduleBloomFilterLoadTrigger,scheduleLoadBloomFilter,bloomfilter[group:" + this
                    .getBloomFilterGroup() + ",name:" +
                    this.getBloomFilterName() + "],order:" + this.getBloomFilterOrder() + "]," +
                    "initDelay[" + initDelay + "],delay[" + delay + "],TimeUnit.SECONDS");
        }
    }

    /**
     * onLoadCommand
     *
     * @param command
     */
    @Subscribe
    public void onLoadCommand(BloomFilterCommand command) {
        if (command != null && command.getAction() == BloomFilterCommand.BloomFilterAction.FORCELOAD) {
            String cmd = command.getCommand();
            if (cmd != null && cmd.equalsIgnoreCase(getBloomFilterName())) {
                triggerLoad(true);
                if (logger.isInfoEnabled()) {
                    logger.info("ScheduleBloomFilterLoadTrigger,onLoadCommand,BloomFilterCommand[" + command + "]");
                }
            }
        }
    }

    /**
     * setInitialDelay
     *
     * @param initialDelay
     */
    public void setInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    /**
     * setDelay
     *
     * @param delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }
}
