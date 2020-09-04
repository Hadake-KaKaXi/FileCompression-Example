package com.gonghf.file.compression.commom;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    /**
     * 递归获取目录下的所有文件
     * @param dir 目录
     * @return 文件列表
     */
    public static List<File> findDirFile(String dir) {
        List<File> filelist = new ArrayList<>();
        File dirfile = new File(dir);
        findFile(dirfile, filelist);
        return filelist;
    }

    private static void findFile(File findfile, List<File> filelist) {
        if (findfile.isFile()) {
            filelist.add(findfile);
        } else {
            File[] files = findfile.listFiles();
            for (File file : files) {
                findFile(file, filelist);
            }
        }
    }
}
