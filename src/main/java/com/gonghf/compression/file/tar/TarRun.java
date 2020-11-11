package com.gonghf.compression.file.tar;

import com.gonghf.compression.commom.Log;

public class TarRun {
    public static void main(String[] args) {
        Log.initLog();
        String dir = "F:/迅雷下载/moban4277/moban4277/examples/demo2";
        String out = "F:/test/tar";
        //压缩
        String compression = TarUtil.tar(dir);

        //解压缩
        TarUtil.unTar(compression, out);
    }
}
