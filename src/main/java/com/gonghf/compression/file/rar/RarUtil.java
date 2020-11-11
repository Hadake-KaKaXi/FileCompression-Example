package com.gonghf.compression.file.rar;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RarUtil {
    public static String rar(String dir) {

        return null;
    }

    /**
     * RAR解压缩
     *
     * @param rarFilePath       rar文件路径
     * @param deCompressionPath 解压到的目录
     * @return
     */
    public static File[] unRar(String rarFilePath, String deCompressionPath) throws IOException {
        File rarFile = new File(rarFilePath);
        if (!rarFile.exists()) {
            return null;
        }
        List<File> fileResults = new ArrayList<>();
        File deCompression = new File(deCompressionPath);

        FileInputStream inputStream = null;
        ZipArchiveInputStream zipInputStream = null;

        try {
            inputStream = new FileInputStream(rarFile);
            zipInputStream = new ZipArchiveInputStream(inputStream);
            ZipArchiveEntry zipEntry = null;

            while ((zipEntry = zipInputStream.getNextZipEntry()) != null){
                System.out.println(zipEntry.getName());
            }

        } finally {
            if (zipInputStream != null) {
                zipInputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return fileResults.toArray(new File[fileResults.size()]);
    }
}
