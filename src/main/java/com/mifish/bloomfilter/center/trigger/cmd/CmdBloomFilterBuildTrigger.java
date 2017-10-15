package com.mifish.bloomfilter.center.trigger.cmd;

import com.google.common.eventbus.Subscribe;
import com.mifish.bloomfilter.center.model.BloomFilterCommand;
import com.mifish.bloomfilter.center.trigger.AbstractBloomFilterBuildTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 14:06
 */
public class CmdBloomFilterBuildTrigger extends AbstractBloomFilterBuildTrigger {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(CmdBloomFilterBuildTrigger.class);

    /**
     * onBuildCommand
     *
     * @param command
     */
    @Subscribe
    public void onBuildCommand(BloomFilterCommand command) {
        if (command != null && command.getAction() == BloomFilterCommand.BloomFilterAction.FORCEBUILD) {
            String cmd = command.getCommand();
            if (cmd != null && cmd.equalsIgnoreCase(getBloomFilterName())) {
                triggerBuild(true);
                if (logger.isInfoEnabled()) {
                    logger.info("CmdBloomFilterBuildTrigger,onBuildCommand,BloomFilterCommand[" + command + "]");
                }
            }
        }
    }
}
