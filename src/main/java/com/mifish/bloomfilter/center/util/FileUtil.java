package com.mifish.bloomfilter.center.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * FileUtil
 *
 * @author rls
 * @time:2013-07-18
 */
public final class FileUtil {

    /**
     * getSimilarFile
     *
     * @param directory
     * @param prefix
     * @return
     */
    public static List<File> getSimilarFile(String directory, String prefix) {
        return getSimilarFile(directory, prefix, false);
    }

    /**
     * getSimilarFile
     *
     * @param directory
     * @param prefix
     * @param asc
     * @return
     */
    public static List<File> getSimilarFile(String directory, String prefix, final boolean asc) {
        if (StringUtils.isBlank(directory)) {
            return Lists.newArrayList();
        }
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            return Lists.newArrayList();
        }
        boolean isFilter = StringUtils.isNotBlank(prefix);
        File[] files = dir.listFiles((d, name) -> {
            return isFilter ? name.startsWith(prefix) : true;
        });
        List<File> filelist = Arrays.asList(files);
        Collections.sort(filelist, (file1, file2) -> {
            long fm1 = ((file1 == null) ? 0 : file1.lastModified());
            long fm2 = ((file2 == null) ? 0 : file2.lastModified());
            if (fm1 == fm2) {
                return 0;
            }
            return asc ? ((fm1 > fm2) ? 1 : -1) : ((fm1 > fm2) ? -1 : 1);
        });
        return filelist;
    }


    /**
     * writeFileBytes
     *
     * @param filepath
     * @param datas
     * @return
     */
    public static boolean writeFileBytes(String filepath, byte[] datas) {
        if (StringUtils.isBlank(filepath)) {
            return false;
        }
        File file = new File(filepath);
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new RuntimeException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new RuntimeException("File '" + file + "' cannot be written to");
            }
            file.delete();
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new RuntimeException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return writeFileBytes(file, datas, false);
    }

    /**
     * writeFileBytes
     *
     * @param file
     * @param datas
     * @param append
     * @return
     */
    public static boolean writeFileBytes(File file, byte[] datas, boolean append) {
        FileOutputStream fos = null;
        try {
            if (file == null || datas == null) {
                return false;
            }
            fos = new FileOutputStream(file, append);
            fos.write(datas);
            fos.flush();
            return true;
        } catch (IOException ex) {
            throw new RuntimeException("FileUtil,writeFileBytes,IOException,FilePath[" + file.getPath() + "]", ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    //ingore
                }
            }
        }
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

    /**
     * FileUtil
     *
     * @throws Exception
     */
    private FileUtil() throws Exception {
        throw new IllegalAccessException("FileUtil cannot be init");
    }
}
