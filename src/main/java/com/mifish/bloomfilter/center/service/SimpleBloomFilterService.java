package com.mifish.bloomfilter.center.service;

import com.mifish.bloomfilter.center.BloomFilterService;
import com.mifish.bloomfilter.center.container.BloomFilterContainer;
import com.mifish.bloomfilter.center.controller.BloomFilterController;
import com.mifish.bloomfilter.center.model.BloomFilterWrapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 14:29
 */
public class SimpleBloomFilterService implements BloomFilterService {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(SimpleBloomFilterService.class);

    /***container*/
    private BloomFilterContainer container;

    /***controller*/
    private BloomFilterController controller;

    @Override
    public boolean isAvailable(String name) {
        boolean result = false;
        if (StringUtils.isNotBlank(name)) {
            result = this.controller.isBloomFilterValid(name) && this.container.isBloomFilterExist(name);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("SimpleBloomFilterService,isAvailable,name[" + name + "],result[" + result + "]");
        }
        return result;
    }

    @Override
    public boolean contains(String name, String key) {
        boolean result = false;
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(key)) {
            BloomFilterWrapper bf = this.container.getBloomFilterByName(name);
            if (bf != null) {
                result = bf.contains(key);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("SimpleBloomFilterService,contains,name[" + name + "],key[" + key + "],result[" + result +
                    "]");
        }
        return result;
    }

    public void setContainer(BloomFilterContainer container) {
        this.container = container;
    }

    public void setController(BloomFilterController controller) {
        this.controller = controller;
    }
}
