package com.gonghf.compression.file.tgz;

import com.gonghf.compression.commom.Log;

import java.io.File;

public class TgzRun {
    public static void main(String[] args) {
        Log.initLog();
        String dir = "F:/迅雷下载/moban4277/moban4277/examples/demo1";
        String out = "F:/test/tar";
        File tgz = TgzUtil.tgz(dir);
        System.out.println(tgz.getAbsolutePath());
    }
}
