package com.gonghf.compression.file.tgz;

import com.gonghf.compression.commom.FileUtil;
import com.gonghf.compression.file.gzip.GzipUtil;
import com.gonghf.compression.file.tar.TarUtil;
import org.apache.log4j.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * tar.gz 压缩类
 */
public class TgzUtil {

    private static Logger logger = Logger.getLogger(TgzUtil.class);

    private static final int TGZ_COMPRESSION_BUFF = 4096;

    private static final int TGZ_DECOMPRESSION_BUFF = 4096;


    public static File tgz(String dirPath) {
        File dirFile = new File(dirPath);
        logger.info("[Tgz压缩开始 >>] " + dirFile.getAbsolutePath());
        if (!dirFile.exists()) {
            logger.error("[Tgz压缩失败] " + dirFile.getAbsolutePath() + "压缩文件不存在");
            return null;
        }

        List<File> fileList = FileUtil.findDirFile(dirPath);
        List<File> gzipFileList = new ArrayList<>();
        logger.info("[Gzip压缩开始 >>] 需要压缩 " + fileList.size() + "个文件");
        for (File file : fileList) {
            File gZip = GzipUtil.gZip(file);
            gzipFileList.add(gZip);
        }
        logger.info("[Gzip压缩结束 <<] 压缩完成 " + gzipFileList.size() + "个文件");
        File tarGz = TarUtil.tarGz(dirFile, gzipFileList);

        for(File gzip:gzipFileList){
            gzip.delete();
        }
        logger.info("[Tgz压缩结束 <<]");
        return tarGz;
    }
}
