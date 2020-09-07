package com.gonghf.file.compression.zip;

import com.gonghf.file.compression.commom.FileUtil;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ZipUtil {

    private static final int ZIP_COMPRESSION_BUFF = 4096;

    private static final int ZIP_DECOMPRESSION_BUFF = 4096;

    /**
     * 将目录/文件压缩成zip
     *
     * @param dir 要压缩的目录或文件
     */
    public static String zip(String dir) throws IOException {
        File file = new File(dir);
        if (!file.exists()) {
            return null;
        }
        List<File> dirFile = FileUtil.findDirFile(dir);

        String fileName = file.getName();
        String parentPath = file.getParentFile().getAbsolutePath();
        //获取压缩后的文件路径
        String zipFilePath = parentPath + File.separator + fileName + ".zip";

        //如果压缩后的文件存在，先进行删除
        File zipFile = new File(zipFilePath);
        if (zipFile.exists()) {
            zipFile.delete();
        }

        zipCompression(dir, dirFile, zipFilePath);
        System.out.println("zipCompression Path: " + zipFilePath);

        return zipFilePath;
    }

    public static File[] unZip(String zipFilePath, String deCompressionPath) throws IOException {
        System.out.println("zipDecompression Path: " + zipFilePath);
        List<File> fileList = new ArrayList<>();

        boolean zipDecompression = zipDecompression(zipFilePath, fileList, deCompressionPath);

        if (zipDecompression) {
            return fileList.toArray(new File[fileList.size()]);
        } else {
            return null;
        }
    }

    /**
     * zip压缩文件/文件夹
     *
     * @param basePath
     * @param fileslist
     * @param zipFilePath
     * @throws IOException
     */
    private static boolean zipCompression(String basePath, List<File> fileslist, String zipFilePath) throws IOException {
        System.out.println("zipCompression -> Compression start!");
        ZipArchiveOutputStream zipArchiveOut = null;
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(new File(zipFilePath));
            zipArchiveOut = new ZipArchiveOutputStream(outputStream);

            for (File file : fileslist) {
                String relativePath = FileUtil.findRelativePath(new File(basePath), file);
                ZipArchiveEntry zipEntry = new ZipArchiveEntry(file, relativePath);
                zipEntry.setSize(file.length());
                System.out.println("    compression file -> " + zipEntry.getName());
                zipArchiveOut.putArchiveEntry(zipEntry);

                FileInputStream inputStream = null;
                BufferedInputStream inputBuff = null;
                try {
                    inputStream = new FileInputStream(file);
                    inputBuff = new BufferedInputStream(inputStream);
                    int count;
                    byte data[] = new byte[ZIP_COMPRESSION_BUFF];
                    while ((count = inputBuff.read(data, 0, ZIP_COMPRESSION_BUFF)) != -1) {
                        zipArchiveOut.write(data, 0, count);
                    }
                } finally {
                    zipArchiveOut.closeArchiveEntry();
                    if (inputBuff != null) {
                        inputBuff.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
            }
        } finally {
            if (zipArchiveOut != null) {
                zipArchiveOut.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
        System.out.println("zipCompression -> Compression complete!");
        return true;
    }

    /**
     * zip解压缩文件/文件夹
     * @param zipFilePath zip文件地址
     * @param fileList  解压后zip的文件列表
     * @param deCompressionPath 要解压到的目录
     * @return
     * @throws IOException
     */
    private static boolean zipDecompression(String zipFilePath, List<File> fileList, String deCompressionPath) throws IOException {
        System.out.println("zipDecompression -> Decompression start!");
        File file = new File(zipFilePath);
        if (!file.exists()) {
            return false;
        }

        ZipArchiveInputStream zipArchiveInput = null;
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
            zipArchiveInput = new ZipArchiveInputStream(fileInput);

            ZipArchiveEntry zipEntry = null;
            while ((zipEntry = zipArchiveInput.getNextZipEntry()) != null) {
                FileOutputStream fileOutput = null;
                BufferedOutputStream outBuff = null;
                try {
                    String dir = deCompressionPath + File.separator + zipEntry.getName();// tar档中文件
                    System.out.println("    already zip decompression file -> " + dir);
                    File dirFile = new File(dir);
                    fileList.add(dirFile);
                    if (!dirFile.getParentFile().exists()) {
                        dirFile.getParentFile().mkdirs();
                    }

                    fileOutput = new FileOutputStream(dir);
                    outBuff = new BufferedOutputStream(fileOutput);

                    int count;
                    byte[] data = new byte[ZIP_DECOMPRESSION_BUFF];

                    while ((count = zipArchiveInput.read(data, 0, ZIP_DECOMPRESSION_BUFF)) != -1) {
                        outBuff.write(data, 0, count);
                    }
                } finally {
                    if (outBuff != null) {
                        outBuff.close();
                    }
                    if (fileOutput != null) {
                        fileOutput.close();
                    }
                }
            }
        } finally {
            if (zipArchiveInput != null) {
                zipArchiveInput.close();
            }
            if (fileInput != null) {
                fileInput.close();
            }
        }
        System.out.println("zipDecompression -> Decompression end!");
        return true;
    }
}
