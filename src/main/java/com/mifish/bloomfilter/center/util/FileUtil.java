package com.mifish.bloomfilter.center.util;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * FileUtil
 *
 * @author rls
 * @time:2013-07-18
 */
public final class FileUtil {

    /**
     * FileUtil
     *
     * @throws Exception
     */
    private FileUtil() throws Exception {
        throw new IllegalAccessException("FileUtil cannot be init");
    }

    /**
     * readFileBytes
     *
     * @param filepath
     * @return
     */
    public static byte[] readFileBytes(String filepath) {
        if (StringUtils.isBlank(filepath)) {
            return null;
        }
        //readFileBytes
        return readFileBytes(new File(filepath));
    }

    /**
     * readFileBytes
     *
     * @param file
     * @return
     */
    public static byte[] readFileBytes(File file) {
        checkArgument(file != null, "FileUtil,readFileBytes,File cannot be null");
        FileInputStream fis = null;
        FileChannel fc = null;
        try {
            fis = new FileInputStream(file);
            fc = fis.getChannel();
            ByteBuffer dest = ByteBuffer.allocate((int) fc.size());
            while (true) {
                if (fc.read(dest) <= 0) {
                    break;
                }
            }
            return dest.array();
        } catch (IOException ex) {
            throw new RuntimeException("FileUtil,readFileBytes,IOException,FilePath[" + file.getPath() + "]", ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    //ignore
                }
            }
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
    }
}
