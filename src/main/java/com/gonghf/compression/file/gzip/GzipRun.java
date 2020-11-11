package com.gonghf.compression.file.gzip;

import com.gonghf.compression.commom.Log;

public class GzipRun {
    public static void main(String[] args) {
        Log.initLog();
        String dir = "F:/迅雷下载/sigar.jar";
        String out = "F:/迅雷下载/sigar";
        String gZip = GzipUtil.gZip(dir);
        System.out.println(gZip);
        String unGzip = GzipUtil.unGzip(gZip, out);
        System.out.println(unGzip);
    }
}
