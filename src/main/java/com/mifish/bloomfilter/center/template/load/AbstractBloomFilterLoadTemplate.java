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
        ConfigMeta configMeta = loadTask.getAttribute("config_meta", ConfigMeta.class);
        Optional<Pair<Long, BloomFilterWrapper>> op = loadAndSerializeLocalBloomFilter(configMeta);
        if (!op.isPresent()) {
            op = loadAndSerializeRemoteBloomFilter(configMeta);
        }
        if (!op.isPresent()) {
            op = loadAndSerializeLastBloomFilter(configMeta);
        }

        if (logger.isInfoEnabled()) {
            logger.info("");
        }
        op.get().getSecond();
        return null;
    }

    /**
     * loadAndSerializeLastBloomFilter
     *
     * @param configMeta
     * @return
     */
    private Optional<Pair<Long, BloomFilterWrapper>> loadAndSerializeLastBloomFilter(ConfigMeta configMeta) {
        return Optional.empty();
    }

    /**
     * loadAndSerializeRemoteBloomFilter
     *
     * @param configMeta
     * @return
     */
    private Optional<Pair<Long, BloomFilterWrapper>> loadAndSerializeRemoteBloomFilter(ConfigMeta configMeta) {
        if (configMeta == null) {
            return Optional.empty();
        }
        String localBFPath = buildLocalBloomFilterPath(configMeta);
        byte[] bfdatas = null;
        int retryTimes = 1;
        boolean isSuccess = false;
        String bfname = configMeta.getName();
        do {
            try {
                BloomFilterWrapper bfwrapper = getBloomFilterInputRepository().queryBloomFilterByName(bfname);
                if (bfwrapper != null) {

                } else {
                    Thread.sleep(200 * retryTimes);
                }
            } catch (Exception ex) {

            }
        } while (!isSuccess && (retryTimes++ < this.loadRetryMaxTime));
        return null;
    }

    /**
     * loadAndSerializeLocalBloomFilter
     *
     * @param configMeta
     * @return
     */
    protected Optional<Pair<Long, BloomFilterWrapper>> loadAndSerializeLocalBloomFilter(ConfigMeta configMeta) {
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
            logger.error("BloomFilterLoadTemplate,loadAndSerializeLocalBloomFilter,");
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
