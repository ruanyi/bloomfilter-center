package com.mifish.bloomfilter.center.template.load;

import com.mifish.bloomfilter.center.model.BloomFilterWrapper;
import com.mifish.bloomfilter.center.model.ConfigMeta;
import com.mifish.bloomfilter.center.model.Pair;
import com.mifish.bloomfilter.center.repository.BloomFilterConfigRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterInputRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterLockRepository;
import com.mifish.bloomfilter.center.repository.BloomFilterOutputRepository;
import com.mifish.bloomfilter.center.serializer.BloomFilterSerializer;
import com.mifish.bloomfilter.center.serializer.impl.SimpleBloomFilterSerializer;
import com.mifish.bloomfilter.center.util.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.mifish.bloomfilter.center.util.BloomFilterUtils.formateBloomFilterDate;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 21:12
 */
public class SimpleBloomFilterLoadTemplate extends AbstractBloomFilterLoadTemplate implements InitializingBean {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(SimpleBloomFilterLoadTemplate.class);

    /***localBFDataDir*/
    private String localBFDataDir = "./local_bf_datas";

    /***loadRetryMaxTime*/
    private int loadRetryMaxTime = 3;

    /***bloomFilterInputRepository*/
    private BloomFilterInputRepository bloomFilterInputRepository;

    /***bloomFilterLockRepository*/
    private BloomFilterLockRepository bloomFilterLockRepository;

    /***bloomFilterConfigRepository*/
    private BloomFilterConfigRepository bloomFilterConfigRepository;

    /***bloomFilterOutputRepository*/
    private BloomFilterOutputRepository bloomFilterOutputRepository;

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
     * loadAndSerializeLastBloomFilter
     *
     * @param taskId
     * @param configMeta
     * @return
     */
    @Override
    protected Optional<Pair<Long, BloomFilterWrapper>> loadAndSerializeLastBloomFilter(String taskId, ConfigMeta
            configMeta) {
        if (configMeta == null) {
            return Optional.empty();
        }
        try {
            String bfname = configMeta.getName();
            //获得一序列：前缀相似的文件，然后
            List<File> files = FileUtil.getSimilarFile(this.localBFDataDir, bfname);
            if (files != null && !files.isEmpty()) {
                //获取第一个文件，也就是：最新的文件
                byte[] bfdatas = loadLocalBloomFilterData(files.get(0));
                BloomFilterWrapper bfwrapper = this.bloomFilterSerializer.deserialize(bfdatas);
                Optional<Pair<Long, BloomFilterWrapper>> op = Optional.empty();
                if (bfwrapper != null) {
                    op = Optional.of(Pair.of(new Long(bfdatas.length), bfwrapper));
                }
                if (logger.isInfoEnabled()) {
                    logger.info("BloomFilterLoadTemplate,loadAndSerializeLastBloomFilter,taskId:" + taskId + "," +
                            "ConfigMeta:"
                            + configMeta + ",len of bfdatas:" + (bfdatas == null ? 0 : bfdatas.length));
                }
                return op;
            }
        } catch (Exception ex) {
            logger.error("BloomFilterLoadTemplate,loadAndSerializeLastBloomFilter,taskId:" + taskId + ",ConfigMeta:"
                    + configMeta + ",exception");
        }
        return Optional.empty();
    }

    /**
     * loadAndSerializeRemoteBloomFilter
     *
     * @param taskId
     * @param configMeta
     * @return
     */
    @Override
    protected Optional<Pair<Long, BloomFilterWrapper>> loadAndSerializeRemoteBloomFilter(String taskId, ConfigMeta
            configMeta) {
        if (configMeta == null) {
            return Optional.empty();
        }
        int retryTimes = 1;
        byte[] bfdatas = null;
        BloomFilterWrapper bfwrapper = null;
        do {
            try {
                bfdatas = this.bloomFilterInputRepository.obtainBloomFilterByUrl(configMeta.getUrl());
                bfwrapper = this.bloomFilterSerializer.deserialize(bfdatas);
                if (bfwrapper == null) {
                    Thread.sleep(200 * retryTimes);
                }
            } catch (Exception ex) {
                logger.error("BloomFilterLoadTemplate,loadAndSerializeRemoteBloomFilter,taskId" + taskId + "," +
                        "retryTimes:" + retryTimes + ",ConfigMeta:" + configMeta + ",exception", ex);
            }
        } while ((bfwrapper == null) && (retryTimes++ < this.loadRetryMaxTime));
        Optional<Pair<Long, BloomFilterWrapper>> op = Optional.empty();
        if (bfwrapper != null) {
            try {
                //保存到本地文件系统中
                String localBFPath = buildLocalBloomFilterPath(configMeta);
                FileUtil.writeFileBytes(localBFPath, bfdatas);
            } catch (Exception ex) {
                //just log warn log message,and continue
                logger.warn("BloomFilterLoadTemplate,loadAndSerializeRemoteBloomFilter,taskId" + taskId + "," +
                        "retryTimes:" + retryTimes + ",ConfigMeta:" + configMeta + ",save file to local path"
                        + buildLocalBloomFilterPath(configMeta) + " error");

            }
            op = Optional.of(Pair.of(new Long(bfdatas.length), bfwrapper));
        }
        if (logger.isInfoEnabled()) {
            logger.info("BloomFilterLoadTemplate,loadAndSerializeRemoteBloomFilter,taskId" + taskId + "," +
                    "retryTimes:" + retryTimes + ",ConfigMeta:" + configMeta + ",len of bloomfilter:" + (bfdatas ==
                    null ? 0 : bfdatas.length));
        }
        return op;
    }

    /**
     * loadAndSerializeLocalBloomFilter
     *
     * @param taskId
     * @param configMeta
     * @return
     */
    @Override
    protected Optional<Pair<Long, BloomFilterWrapper>> loadAndSerializeLocalBloomFilter(String taskId, ConfigMeta
            configMeta) {
        if (configMeta == null) {
            return Optional.empty();
        }
        String localBFPath = buildLocalBloomFilterPath(configMeta);
        try {
            byte[] bfdatas = loadLocalBloomFilterData(localBFPath);
            BloomFilterWrapper bfwrapper = this.bloomFilterSerializer.deserialize(bfdatas);
            Optional<Pair<Long, BloomFilterWrapper>> op = Optional.empty();
            if (bfwrapper != null) {
                op = Optional.of(Pair.of(new Long(bfdatas.length), bfwrapper));
            }
            if (logger.isInfoEnabled()) {
                logger.info("BloomFilterLoadTemplate,loadAndSerializeLocalBloomFilter,taskId:" + taskId + ",ConfigMeta:"
                        + configMeta + ",len of bfdatas:" + (bfdatas == null ? 0 : bfdatas.length));
            }
            return op;
        } catch (Exception ex) {
            logger.error("BloomFilterLoadTemplate,loadAndSerializeLocalBloomFilter,taskId:" + taskId + ",ConfigMeta:"
                    + configMeta + ",exception");
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
     * <p>
     * 构建布隆索引的文件地址
     *
     * @param configMeta
     * @return
     */
    private String buildLocalBloomFilterPath(ConfigMeta configMeta) {
        return this.localBFDataDir + "/" + configMeta.getName() + "_" +
                formateBloomFilterDate(configMeta.getTimeVersion()) + ".data";
    }

    /**
     * getBloomFilterLockRepository
     *
     * @return
     */
    @Override
    public BloomFilterLockRepository getBloomFilterLockRepository() {
        return this.bloomFilterLockRepository;
    }

    /**
     * getBloomFilterConfigRepository
     *
     * @return
     */
    @Override
    public BloomFilterConfigRepository getBloomFilterConfigRepository() {
        return this.bloomFilterConfigRepository;
    }

    /**
     * getBloomFilterOutputRepository
     *
     * @return
     */
    @Override
    public BloomFilterOutputRepository getBloomFilterOutputRepository() {
        return this.bloomFilterOutputRepository;
    }

    /**
     * getBloomFilterInputRepository
     *
     * @return
     */
    @Override
    public BloomFilterInputRepository getBloomFilterInputRepository() {
        return this.bloomFilterInputRepository;
    }

    /**
     * setBloomFilterInputRepository
     *
     * @param bloomFilterInputRepository
     */
    public void setBloomFilterInputRepository(BloomFilterInputRepository bloomFilterInputRepository) {
        this.bloomFilterInputRepository = bloomFilterInputRepository;
    }

    /**
     * setBloomFilterLockRepository
     *
     * @param bloomFilterLockRepository
     */
    public void setBloomFilterLockRepository(BloomFilterLockRepository bloomFilterLockRepository) {
        this.bloomFilterLockRepository = bloomFilterLockRepository;
    }

    /**
     * setBloomFilterConfigRepository
     *
     * @param bloomFilterConfigRepository
     */
    public void setBloomFilterConfigRepository(BloomFilterConfigRepository bloomFilterConfigRepository) {
        this.bloomFilterConfigRepository = bloomFilterConfigRepository;
    }

    /**
     * setBloomFilterOutputRepository
     *
     * @param bloomFilterOutputRepository
     */
    public void setBloomFilterOutputRepository(BloomFilterOutputRepository bloomFilterOutputRepository) {
        this.bloomFilterOutputRepository = bloomFilterOutputRepository;
    }
}
