package com.gonghf.file.compression.tar;

import com.gonghf.file.compression.commom.FileUtil;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TarUtil {

    /**
     * @param dir 需要打包的目录或文件
     * @return 打包后tar包的地址
     * @throws Exception
     */
    public static String compression(String dir) throws Exception {
        List<File> dirFile = FileUtil.findDirFile(dir);
        File file = new File(dir);
        String parentPath = file.getParentFile().getAbsolutePath();
        String fileName = file.getName();
        String resultFilePath = parentPath + File.separator + fileName + ".tar";
        File resultFile = new File(resultFilePath);
        if (resultFile.exists()) {
            resultFile.delete();
        }
        boolean compression = tarCompression(dir,dirFile.toArray(new File[dirFile.size()]), resultFilePath);
        if (compression) {
            return resultFilePath;
        } else {
            return null;
        }
    }

    /**
     * @param tarFile tar包地址
     * @param outdir 解压缩的目录
     * @return 解压后所有文件的集合
     * @throws Exception
     */
    public static File[] decompression(String tarFile, String outdir) throws Exception {
        ArrayList<File> filelist = new ArrayList<>();
        boolean decompression = tarDecompression(tarFile, outdir, filelist);
        if (decompression) {
            return filelist.toArray(new File[filelist.size()]);
        } else {
            return null;
        }
    }

    /**
     * tar打包压缩
     *
     * @param filesArray     要压缩的文件的全路径(数组)
     * @param resultFilePath 压缩后的文件全文件名(.tar)
     * @throws Exception
     * @DATE 2018年9月25日 下午12:39:28
     */
    private static boolean tarCompression(String basePath,File[] filesArray, String resultFilePath) throws Exception {
        System.out.println(" tarCompression -> Compression start!");
        FileOutputStream fos = null;
        TarArchiveOutputStream taos = null;
        try {
            fos = new FileOutputStream(new File(resultFilePath));
            taos = new TarArchiveOutputStream(fos);
            for (File file : filesArray) {
                BufferedInputStream bis = null;
                FileInputStream fis = null;
                try {
                    TarArchiveEntry tae = new TarArchiveEntry(file);
                    tae.setSize(file.length());
                    String tarPath = file.getAbsolutePath().replaceAll(basePath, "");
                    tae.setName(file.getAbsolutePath());
                    taos.putArchiveEntry(tae);
                    System.out.println("  compression file -> " + tae.getName());
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    int count;
                    byte data[] = new byte[1024];
                    while ((count = bis.read(data, 0, 1024)) != -1) {
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
        System.out.println(" tarCompression -> Compression end!");
        return true;
    }

    /**
     * tar拆包解压
     *
     * @param decompressFilePath 要被解压的压缩文件 全路径
     * @param resultDirPath      解压文件存放绝对路径(目录)
     * @throws Exception
     * @DATE 2018年9月25日 下午12:39:43
     */
    private static boolean tarDecompression(String decompressFilePath, String resultDirPath, List<File> fileList) throws Exception {
        System.out.println(" tarDecompression -> Decompression start!");
        TarArchiveInputStream tais = null;
        FileInputStream fis = null;
        try {
            File file = new File(decompressFilePath);
            fis = new FileInputStream(file);
            tais = new TarArchiveInputStream(fis);
            TarArchiveEntry tae = null;
            while ((tae = tais.getNextTarEntry()) != null) {
                BufferedOutputStream bos = null;
                FileOutputStream fos = null;
                try {
                    System.out.println("  already decompression file -> " + tae.getName());
                    String dir = resultDirPath + File.separator + tae.getName();// tar档中文件
                    File dirFile = new File(dir);
                    fileList.add(dirFile);
                    if(!dirFile.getParentFile().exists()){
                        dirFile.getParentFile().mkdirs();
                    }
                    fos = new FileOutputStream(dirFile);
                    bos = new BufferedOutputStream(fos);
                    int count;
                    byte[] data = new byte[1024];
                    while ((count = tais.read(data, 0, 1024)) != -1) {
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
        System.out.println(" tarDecompression -> Decompression end!");
        return true;
    }
}
