package com.mifish.bloomfilter.center.template.build;

import com.mifish.bloomfilter.center.controller.BloomFilterController;
import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.model.BloomFilterWrapper;
import com.mifish.bloomfilter.center.model.ConfigMeta;
import com.mifish.bloomfilter.center.model.QueryIdRange;
import com.mifish.bloomfilter.center.template.BloomFilterBuildTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static com.mifish.bloomfilter.center.util.BloomFilterUtils.formateBloomFilterDate;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 21:25
 */
public abstract class AbstractBloomFilterBuildTemplate implements BloomFilterBuildTemplate {

    /***LOGGER*/
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBloomFilterBuildTemplate.class);

    /***bloomFilterController*/
    protected BloomFilterController bloomFilterController;

    /**
     * build
     *
     * @param buildTask
     * @return
     */
    @Override
    public BloomFilterTaskResult build(BloomFilterTask buildTask) {
        long start = System.currentTimeMillis();
        if (buildTask == null || !buildTask.isBuildTask()) {
            return BloomFilterTaskResult.FAILURE(-1, "buildTask is illegal");
        }
        String bfname = buildTask.getBloomFilterName();
        String taskId = buildTask.getAttribute("taskId", String.class);
        Date timeVersion = buildTask.getAttribute("timeVersion", Date.class);
        ConfigMeta configMeta = buildTask.getAttribute("config_meta", ConfigMeta.class);
        try {
            //1、获得构建布隆索引的开始时间
            Date startTime = obtainBloomFilterStartTime(buildTask, configMeta);
            //2、获得构建布隆索引的结束时间
            Date endTime = obtainBloomFilterEndTime(buildTask, configMeta);
            //3、获得本次构建布隆索引的时间范围
            QueryIdRange idRange = queryBloomFilterDataRange(buildTask, startTime, endTime);
            //4、校验参数
            if (idRange == null) {
                return BloomFilterTaskResult.FAILURE(-2, "idRange is illegal");
            }
            //5、获得或者重新构建BloomFilterWrapper
            BloomFilterWrapper bfwrapper = obtainOrBuildBloomFilterWrapper(buildTask, idRange);
            //6、添加到布隆索引中，并发或者单线程添加，具体看配置
            Date ntv = add2BloomFilter(buildTask, startTime, endTime, bfwrapper, idRange);
            //7、构建过程中，可能会出现异常，从新设置版本号:timeVersion
            bfwrapper.setTimeVersion(ntv);
            //8、打印本次构建的详细信息
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("BloomFilterBuildTemplate,build,taskId:" + taskId + ",bfname:" + bfname + ",timeVersion:"
                        + formateBloomFilterDate(timeVersion) + ",new timeVersion:" + formateBloomFilterDate(ntv) +
                        ",Y,cost:" + (System.currentTimeMillis() - start));
            }
            //9、return success result
            return BloomFilterTaskResult.SUCCESS(bfwrapper);
        } catch (Exception ex) {
            //10、log exception msg
            LOGGER.error("BloomFilterBuildTemplate,build,taskId:" + taskId + ",bfname:" + bfname + ",timeVersion:"
                            + formateBloomFilterDate(timeVersion) + ",exception,cost:" + (System.currentTimeMillis()
                            - start)
                    , ex);
            return BloomFilterTaskResult.FAILURE(-9, "");
        }
    }

    /**
     * obtainOrBuildBloomFilterWrapper
     * <p>
     * 如果是头任务，则重新构建
     * 如果是子任务：则重新构建
     * 否则，用上一个版本，继续添加数据
     *
     * @param buildTask
     * @param idRange
     * @return
     */
    private BloomFilterWrapper obtainOrBuildBloomFilterWrapper(BloomFilterTask buildTask, QueryIdRange
            idRange) {
        String bfname = buildTask.getBloomFilterName();
        String taskId = buildTask.getAttribute("taskId", String.class);
        Date timeVersion = buildTask.getAttribute("timeVersion", Date.class);
        BloomFilterWrapper bfwrapper = null;
        if (buildTask.getPreTask() == null) {
            bfwrapper = new BloomFilterWrapper(this.bloomFilterController.obtainBloomFilterPositiveProbability
                    (bfname), idRange.getCount(), this.bloomFilterController.obtainBloomFilterCharset(bfname),
                    timeVersion);
        } else if (buildTask.isSubTask()) {
            bfwrapper = new BloomFilterWrapper(this.bloomFilterController.obtainBloomFilterPositiveProbability
                    (bfname), this.bloomFilterController.obtainBloomFilterBuildInitCount(bfname), this
                    .bloomFilterController.obtainBloomFilterCharset(bfname),
                    timeVersion);
        } else {
            bfwrapper = obtainBloomFilterWrapper(buildTask);
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("BloomFilterBuildTemplate,obtainOrBuildBloomFilterWrapper,bfname:" + bfname + ",taskId:" +
                    taskId + ",Y");
        }
        return bfwrapper;
    }

    /**
     * add2BloomFilter
     *
     * @param buildTask
     * @param startTime
     * @param endTime
     * @param bfwrapper
     * @param idRange
     * @return
     */
    protected abstract Date add2BloomFilter(BloomFilterTask buildTask, Date startTime, Date endTime,
                                            BloomFilterWrapper bfwrapper, QueryIdRange idRange);

    /**
     * queryBloomFilterDataRange
     *
     * @param buildTask
     * @param startTime
     * @param endTime
     * @return
     */
    protected abstract QueryIdRange queryBloomFilterDataRange(BloomFilterTask buildTask, Date startTime, Date endTime);

    /**
     * obtainBloomFilterEndTime
     *
     * @param buildTask
     * @param configMeta
     * @return
     */
    private Date obtainBloomFilterEndTime(BloomFilterTask buildTask, ConfigMeta configMeta) {
        String bfname = buildTask.getBloomFilterName();
        String taskId = buildTask.getAttribute("taskId", String.class);
        Date timeVersion = buildTask.getAttribute("timeVersion", Date.class);
        Date endTime = timeVersion;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("BloomFilterBuildTemplate,build,obtainBloomFilterEndTime,bfname:" + bfname + ",taskId:" +
                    taskId + ",old timeVersion:" + formateBloomFilterDate(configMeta.getTimeVersion()) + ",end time:"
                    + formateBloomFilterDate(endTime));
        }
        return endTime;
    }

    /**
     * obtainBloomFilterStartTime
     * <p>
     * 只要是子任务，其startTime都是前置任务：布隆索引的timeVersion
     * 当为头任务时：分为两种情况：a、在整个任务链中，第一个一定为：null
     * b、否则，是它本身任务的上一个：timeVersion
     *
     * @param buildTask
     * @param configMeta
     * @return
     */
    private Date obtainBloomFilterStartTime(BloomFilterTask buildTask, ConfigMeta configMeta) {
        String bfname = buildTask.getBloomFilterName();
        String taskId = buildTask.getAttribute("taskId", String.class);
        Date startTime = null;
        if (buildTask.isSubTask()) {
            BloomFilterTask preBuildTask = buildTask.getPreTask();
            startTime = preBuildTask.getAttribute("timeVersion", Date.class);
        } else if (buildTask.getPreTask() != null) {
            startTime = configMeta.getTimeVersion();
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("BloomFilterBuildTemplate,build,obtainBloomFilterStartTime,bfname:" + bfname + ",taskId:" +
                    taskId + ",isSubTask:" + buildTask.isSubTask() + ",is head task:" + (buildTask.getPreTask() !=
                    null) + ",return startTime:" + formateBloomFilterDate(startTime));
        }
        return startTime;
    }

    /**
     * obtainBloomFilterWrapper
     *
     * @param buildTask
     * @return
     */
    protected abstract BloomFilterWrapper obtainBloomFilterWrapper(BloomFilterTask buildTask);

    /**
     * setBloomFilterController
     *
     * @param bloomFilterController
     */
    public void setBloomFilterController(BloomFilterController bloomFilterController) {
        this.bloomFilterController = bloomFilterController;
    }
}
