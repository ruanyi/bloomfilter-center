package com.mifish.bloomfilter.center.controller.impl;

import com.mifish.bloomfilter.center.controller.BloomFilterController;

/**
 * Description:
 * <p>
 * 通过properties来控制
 *
 * @author: rls
 * Date: 2017-11-05 15:04
 */
public class BloomFilterPropertiesController implements BloomFilterController {

    @Override
    public boolean isBloomFilterValid(String bfname) {
        return false;
    }
}
