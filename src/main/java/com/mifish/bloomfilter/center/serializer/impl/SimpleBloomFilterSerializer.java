package com.mifish.bloomfilter.center.serializer.impl;

import com.mifish.bloomfilter.center.model.BloomFilterWrapper;
import com.mifish.bloomfilter.center.serializer.BloomFilterSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 13:15
 */
public class SimpleBloomFilterSerializer implements BloomFilterSerializer {

    @Override
    public byte[] serialize(BloomFilterWrapper wrapper) {
        if (wrapper == null) {
            return null;
        }
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(wrapper);
            oos.flush();
            return bos.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("SimpleBloomFilterSerializer,serialize,Exception", ex);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                    oos = null;//help gc
                    bos = null;//help gc
                } catch (IOException e) {
                    //ingore
                }
            }
        }
    }


    @Override
    public BloomFilterWrapper deserialize(byte[] datas) {
        if (datas == null || datas.length == 0) {
            return null;
        }
        ObjectInputStream ois = null;
        ByteArrayInputStream bis = null;
        try {
            bis = new ByteArrayInputStream(datas);
            ois = new ObjectInputStream(bis);
            return (BloomFilterWrapper) ois.readObject();
        } catch (Exception ex) {
            throw new RuntimeException("SimpleBloomFilterSerializer,deserialize,Exception.", ex);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                    ois = null;//help gc
                    bis = null;//help gc
                } catch (IOException e) {
                    //ingore exception
                }
            }
        }
    }

    /***SimpleBloomFilterSerializerHolder*/
    private static class SimpleBloomFilterSerializerHolder {
        private static SimpleBloomFilterSerializer serializer = new SimpleBloomFilterSerializer();
    }

    /***getInstance*/
    public static BloomFilterSerializer getInstance() {
        return SimpleBloomFilterSerializerHolder.serializer;
    }
}
