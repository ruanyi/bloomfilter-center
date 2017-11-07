package com.mifish.bloomfilter.center.template.load;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskResult;
import com.mifish.bloomfilter.center.model.BloomFilterWrapper;
import com.mifish.bloomfilter.center.model.ConfigMeta;
import com.mifish.bloomfilter.center.model.Pair;
import com.mifish.bloomfilter.center.serializer.BloomFilterSerializer;
import com.mifish.bloomfilter.center.serializer.impl.SimpleBloomFilterSerializer;
import com.mifish.bloomfilter.center.template.BloomFilterLoadTemplate;
import com.mifish.bloomfilter.center.util.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-05 20:27
 */
public abstract class AbstractBloomFilterLoadTemplate implements BloomFilterLoadTemplate, InitializingBean {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(AbstractBloomFilterLoadTemplate.class);

    /***localBFDataDir*/
    private String localBFDataDir = "./local_bf_datas";

    /***loadRetryMaxTime*/
    private int loadRetryMaxTime = 3;

    /***bloomFilterSerializer*/
    private BloomFilterSerializer bloomFilterSerializer = SimpleBloomFilterSerializer.getInstance();


    @Override
    public void afterPropertiesSet() throws Exception {
        ensureBFDataDir();
    }

    /**
     * ensureBFDataDir
     *
     * @throws IOException
     */
    private void ensureBFDataDir() throws IOException {
        if (StringUtils.isBlank(this.localBFDataDir)) {
            throw new IOException("SimpleBloomFilterLoadTemplate,ensureBFDataDir,localBFDataDir cannot be blank!");
        }
        File file = new File(this.localBFDataDir);
        if (!file.exists() || !file.isDirectory()) {
            file.delete();
            file.mkdir();
        }
    }

    /**
     * load
     *
     * @param loadTask
     * @param startTaskTime
     * @return
     */
    @Override
    public BloomFilterTaskResult load(BloomFilterTask loadTask, Date startTaskTime) {
        if (loadTask == null || !loadTask.isLoadTask()) {
            return null;
        }
        String bfname = loadTask.getBloomFilterName();
        String taskId = loadTask.getAttribute("taskId", String.class);
        ConfigMeta configMeta = loadTask.getAttribute("config_meta", ConfigMeta.class);
        Optional<Pair<Long, BloomFilterWrapper>> op = loadAndSerializeLocalBloomFilter(taskId, configMeta);
        if (!op.isPresent()) {
            op = loadAndSerializeRemoteBloomFilter(taskId, configMeta);
        }
        if (!op.isPresent()) {
            op = loadAndSerializeLastBloomFilter(taskId, configMeta);
        }
        BloomFilterTaskResult result = null;
        if (op.isPresent()) {
            long bloomfilterFileSize = op.get().getFirst();
            BloomFilterWrapper blwrapper = op.get().getSecond();
            blwrapper.setBloomfilterFileSize(bloomfilterFileSize);
            blwrapper.setTimeVersion(configMeta.getTimeVersion());
            result = BloomFilterTaskResult.SUCCESS(op.get().getSecond());
        } else {
            result = BloomFilterTaskResult.FAILURE(-9);
        }
        if (logger.isInfoEnabled()) {
            logger.info("BloomFilterLoadTemplate,load,taskId" + taskId + ",bfname:" + bfname + ",");
        }
        return result;
    }

    /**
     * loadAndSerializeLastBloomFilter
     *
     * @param taskId
     * @param configMeta
     * @return
     */
    private Optional<Pair<Long, BloomFilterWrapper>> loadAndSerializeLastBloomFilter(String taskId, ConfigMeta
            configMeta) {
        return Optional.empty();
    }

    /**
     * loadAndSerializeRemoteBloomFilter
     *
     * @param taskId
     * @param configMeta
     * @return
     */
    private Optional<Pair<Long, BloomFilterWrapper>> loadAndSerializeRemoteBloomFilter(String taskId, ConfigMeta
            configMeta) {
        if (configMeta == null) {
            return Optional.empty();
        }

        int retryTimes = 1;
        byte[] bfdatas = null;
        BloomFilterWrapper bfwrapper = null;
        String bfname = configMeta.getName();
        do {
            try {
                bfdatas = getBloomFilterInputRepository().queryBloomFilterByName(bfname);
                bfwrapper = this.bloomFilterSerializer.deserialize(bfdatas);
                if (bfwrapper == null) {
                    Thread.sleep(200 * retryTimes);
                }
            } catch (Exception ex) {
                logger.error("BloomFilterLoadTemplate,loadAndSerializeRemoteBloomFilter,taskId" + taskId + "," +
                        "retryTimes:" + retryTimes + ",exception", ex);
            }
        } while ((bfwrapper == null) && (retryTimes++ < this.loadRetryMaxTime));
        if (bfwrapper != null) {
            //保存到本地文件系统中
            String localBFPath = buildLocalBloomFilterPath(configMeta);
            //
            return Optional.of(Pair.of(new Long(bfdatas.length), bfwrapper));
        }
        return Optional.empty();
    }

    /**
     * loadAndSerializeLocalBloomFilter
     *
     * @param taskId
     * @param configMeta
     * @return
     */
    protected Optional<Pair<Long, BloomFilterWrapper>> loadAndSerializeLocalBloomFilter(String taskId, ConfigMeta
            configMeta) {
        if (configMeta == null) {
            return Optional.empty();
        }
        String localBFPath = buildLocalBloomFilterPath(configMeta);
        try {
            byte[] bfdatas = loadLocalBloomFilterData(localBFPath);
            BloomFilterWrapper bfwrapper = this.bloomFilterSerializer.deserialize(bfdatas);
            if (bfwrapper != null) {
                return Optional.of(Pair.of(new Long(bfdatas.length), bfwrapper));
            }
            return Optional.empty();
        } catch (Exception ex) {
            logger.error("BloomFilterLoadTemplate,loadAndSerializeLocalBloomFilter,taskId:" + taskId + ",exception");
            return Optional.empty();
        }
    }

    /**
     * loadLocalBloomFilterData
     *
     * @param localBFPath
     * @return
     */
    private byte[] loadLocalBloomFilterData(String localBFPath) {
        if (StringUtils.isBlank(localBFPath)) {
            return null;
        }
        //loadLocalBloomFilterData
        return loadLocalBloomFilterData(new File(localBFPath));
    }

    /**
     * loadLocalBloomFilterData
     *
     * @param file
     * @return
     */
    private byte[] loadLocalBloomFilterData(File file) {
        if (file == null) {
            return null;
        }
        if (file.exists() && file.isFile()) {
            return FileUtil.readFileBytes(file);
        } else {
            file.delete();
            return null;
        }
    }

    /**
     * buildLocalBloomFilterPath
     *
     * @param configMeta
     * @return
     */
    private String buildLocalBloomFilterPath(ConfigMeta configMeta) {
        return "";
    }
}
