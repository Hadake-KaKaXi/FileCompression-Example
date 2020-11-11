package com.gonghf.compression.file.gzip;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipUtils;
import org.apache.log4j.Logger;

import java.io.*;


/**
 * Gzip 压缩工具类 (只针对单个文件)
 */
public class GzipUtil {

    private static Logger logger = Logger.getLogger(GzipUtil.class);

    private static final int GZIP_COMPRESSION_BUFF = 4096;

    private static final int GZIP_DECOMPRESSION_BUFF = 4096;

    public static String gZip(String filePath) {
        File inputFile = new File(filePath);
        File gZip = gZip(inputFile);
        return gZip.getAbsolutePath();
    }

    public static File gZip(File inputFile) {
        logger.info("[Gzip压缩开始 >>] " + inputFile.getAbsolutePath());
        if (!inputFile.exists()) {
            logger.error("[Gzip压缩失败] " + inputFile.getAbsolutePath() + "压缩文件不存在");
            return null;
        }

        if (inputFile.isDirectory()) {
            logger.error("[Gzip压缩失败] " + inputFile.getAbsolutePath() + "仅支持文件压缩");
            return null;
        }

        String gZipFilePath = GzipUtils.getCompressedFilename(inputFile.getAbsolutePath());
        File gZipFile = new File(gZipFilePath);
        if (gZipFile.exists()) {
            gZipFile.delete();
        }

        try {
            GzipCompression(inputFile, gZipFile);
        } catch (IOException e) {
            logger.error("[Gzip压缩失败] " + inputFile.getAbsolutePath() + " :" + e.getMessage());
        }

        logger.info("[Gzip压缩结束 <<] 完成文件压缩");
        return gZipFile;
    }

    public static String unGzip(String gZipPath, String outPath) {
        logger.info("[Gzip解压开始 >>] " + gZipPath + " 解压到 " + outPath);
        File gZipFile = new File(gZipPath);
        if (!gZipFile.exists()) {
            logger.error("[Gzip解压失败] " + gZipFile.getAbsolutePath() + "解压文件不存在");
            return null;
        }
        String deCompressionPath = null;
        try {
            deCompressionPath = GzipDeCompression(gZipFile, outPath);
        } catch (IOException e) {
            logger.error("[Gzip解压失败] " + gZipFile.getAbsolutePath() + " :" + e.getMessage());
        }
        if (deCompressionPath != null) {
            logger.info("[Gzip解压结束 <<] 完成解压: " + deCompressionPath);
        } else {
            logger.info("[Gzip解压结束 <<] 解压失败");
        }
        return deCompressionPath;
    }

    private static boolean GzipCompression(File inputFile, File gZipFile) throws IOException {
        logger.debug("[Gzip压缩文件] 压缩文件" + inputFile.getAbsolutePath());
        FileInputStream inputStream = null;
        BufferedInputStream inputStreamBuff = null;
        try {
            inputStream = new FileInputStream(inputFile);
            inputStreamBuff = new BufferedInputStream(inputStream);

            FileOutputStream outputStream = null;
            GzipCompressorOutputStream gZipOutputStream = null;
            try {
                outputStream = new FileOutputStream(gZipFile);
                gZipOutputStream = new GzipCompressorOutputStream(outputStream);

                int count;
                byte[] data = new byte[GZIP_COMPRESSION_BUFF];

                while ((count = inputStreamBuff.read(data, 0, GZIP_COMPRESSION_BUFF)) != -1) {
                    gZipOutputStream.write(data, 0, count);
                }

                gZipOutputStream.finish();
                gZipOutputStream.flush();
            } finally {
                if (gZipOutputStream != null) {
                    gZipOutputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } finally {
            if (inputStreamBuff != null) {
                inputStreamBuff.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return true;
    }

    private static String GzipDeCompression(File gZipFile, String outPath) throws IOException {
        logger.debug("[Gzip解压文件] 解压文件" + gZipFile.getAbsolutePath());
        GzipCompressorInputStream gZipInputStream = null;
        FileInputStream inputStream = null;
        File outFile = null;
        try {
            inputStream = new FileInputStream(gZipFile);
            gZipInputStream = new GzipCompressorInputStream(inputStream);
            String filename = gZipInputStream.getMetaData().getFilename();
            if (filename == null) {
                filename = new File(GzipUtils.getUncompressedFilename(gZipFile.getAbsolutePath())).getName();
            }
            String newOutPath = outPath + File.separator + filename;
            outFile = new File(newOutPath);

            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            if (outFile.exists()) {
                outFile.delete();
            }

            FileOutputStream outputStream = null;
            BufferedOutputStream outputStreamBuff = null;
            try {
                outputStream = new FileOutputStream(newOutPath);
                outputStreamBuff = new BufferedOutputStream(outputStream);

                int count;
                byte[] data = new byte[GZIP_DECOMPRESSION_BUFF];

                while ((count = gZipInputStream.read(data, 0, GZIP_DECOMPRESSION_BUFF)) != -1) {
                    outputStreamBuff.write(data, 0, count);
                }
            } finally {
                if (outputStreamBuff != null) {
                    outputStreamBuff.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }

        } finally {
            if (gZipInputStream != null) {
                gZipInputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return outFile.getAbsolutePath();
    }
}
