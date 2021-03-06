package com.gonghf.compression.commom;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    /**
     * 递归获取目录下的所有文件
     *
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

    public static String findRelativePath(File base, File file) {
        //格式化基准目录
        String basePathFormat = base.getAbsolutePath();
        //格式化文件目录
        String filePathFormat = file.getAbsolutePath();
        if (basePathFormat.equals(filePathFormat)) {
            //一个单文件的情况
            return file.getName();
        } else {
            //截取相对路径
            String relative = filePathFormat.replace(basePathFormat, "");

            String relativePath = relative.substring(1, relative.length()).replaceAll("\\\\", "/");

            return relativePath;
        }

    }
}
