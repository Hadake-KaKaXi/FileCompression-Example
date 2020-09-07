package com.gonghf.file.compression.tar;

import com.gonghf.file.compression.commom.FileUtil;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TarUtil {

    private static final int TAR_COMPRESSION_BUFF = 4096;

    private static final int TAR_DECOMPRESSION_BUFF = 4096;

    /**
     * @param dir 需要压缩的目录或文件
     * @return 打包后tar包的地址
     * @throws Exception
     */
    public static String tar(String dir) throws Exception {
        File file = new File(dir);
        if (!file.exists()) {
            return null;
        }
        //获取目录下所有文件
        List<File> dirFiles = FileUtil.findDirFile(dir);

        String fileName = file.getName();
        String parentPath = file.getParentFile().getAbsolutePath();
        //获取压缩后的文件路径
        String tarFilePath = parentPath + File.separator + fileName + ".tar";

        //如果压缩后的文件存在，先进行删除
        File tarFile = new File(tarFilePath);
        if (tarFile.exists()) {
            tarFile.delete();
        }

        boolean compression = tarCompression(dir, dirFiles, tarFilePath);
        if (compression) {
            System.out.println("tarCompression Path: " + tarFilePath);
            return tarFilePath;
        } else {
            return null;
        }
    }

    /**
     * @param tarFilePath tar包地址
     * @param outdir 解压缩的目录
     * @return 解压后所有文件的集合
     * @throws Exception
     */
    public static File[] unTar(String tarFilePath, String outdir) throws Exception {
        //解压后的文件集合
        List<File> filelist = new ArrayList<>();
        //解压文件
        System.out.println("tarDecompression Path: " + tarFilePath);
        boolean decompression = tarDecompression(tarFilePath, filelist, outdir);
        if (decompression) {
            return filelist.toArray(new File[filelist.size()]);
        } else {
            return null;
        }
    }

    /**
     * tar打包压缩
     *
     * @param fileslist   要压缩的文件的全路径(集合)
     * @param tarFilePath 压缩后的文件全文件名(.tar)
     * @throws Exception
     * @DATE 2018年9月25日 下午12:39:28
     */
    private static boolean tarCompression(String basePath, List<File> fileslist, String tarFilePath) throws Exception {
        System.out.println("tarCompression -> Compression start!");
        FileOutputStream fos = null;
        TarArchiveOutputStream taos = null;
        try {
            fos = new FileOutputStream(new File(tarFilePath));
            taos = new TarArchiveOutputStream(fos);
            for (File file : fileslist) {
                BufferedInputStream bis = null;
                FileInputStream fis = null;
                try {
                    TarArchiveEntry tae = new TarArchiveEntry(file);
                    tae.setSize(file.length());
                    String tarPath = FileUtil.findRelativePath(new File(basePath), file);
                    tae.setName(tarPath);
                    taos.putArchiveEntry(tae);
                    System.out.println("    compression file -> " + tae.getName());
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    int count;
                    byte data[] = new byte[TAR_COMPRESSION_BUFF];
                    while ((count = bis.read(data, 0, TAR_COMPRESSION_BUFF)) != -1) {
                        taos.write(data, 0, count);
                    }
                } finally {
                    taos.closeArchiveEntry();
                    if (bis != null)
                        bis.close();
                    if (fis != null)
                        fis.close();
                }
            }
        } finally {
            if (taos != null)
                taos.close();
            if (fos != null)
                fos.close();
        }
        System.out.println("tarCompression -> Compression end!");
        return true;
    }

    /**
     * tar拆包解压
     *
     * @param tarFilePath       要被解压的压缩文件 全路径
     * @param deCompressionPath 解压文件存放绝对路径(目录)
     * @throws Exception
     * @DATE 2018年9月25日 下午12:39:43
     */
    private static boolean tarDecompression(String tarFilePath, List<File> fileList, String deCompressionPath) throws Exception {
        System.out.println("tarDecompression -> Decompression start!");
        File file = new File(tarFilePath);
        if (!file.exists()) {
            return false;
        }
        TarArchiveInputStream tais = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            tais = new TarArchiveInputStream(fis);
            TarArchiveEntry tae = null;
            while ((tae = tais.getNextTarEntry()) != null) {
                BufferedOutputStream bos = null;
                FileOutputStream fos = null;
                try {
                    String dir = deCompressionPath + File.separator + tae.getName();// tar档中文件
                    System.out.println("    already tar decompression file -> " + dir);
                    File dirFile = new File(dir);
                    fileList.add(dirFile);
                    if (!dirFile.getParentFile().exists()) {
                        dirFile.getParentFile().mkdirs();
                    }
                    fos = new FileOutputStream(dirFile);
                    bos = new BufferedOutputStream(fos);
                    int count;
                    byte[] data = new byte[TAR_DECOMPRESSION_BUFF];
                    while ((count = tais.read(data, 0, TAR_DECOMPRESSION_BUFF)) != -1) {
                        bos.write(data, 0, count);
                    }
                } finally {
                    if (bos != null)
                        bos.close();
                    if (fos != null)
                        fos.close();
                }
            }
        } finally {
            if (tais != null)
                tais.close();
            if (fis != null)
                fis.close();
        }
        System.out.println("tarDecompression -> Decompression end!");
        return true;
    }
}
