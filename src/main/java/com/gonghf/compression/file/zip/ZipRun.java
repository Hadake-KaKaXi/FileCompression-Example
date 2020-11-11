package com.gonghf.compression.file.zip;

import com.gonghf.compression.commom.Log;

public class ZipRun {
    public static void main(String[] args) {
        Log.initLog();
        //String dir = "F:/迅雷下载/moban4277/moban4277/examples/demo2";

        String out = "F:/test/dsp";

        //压缩
        //String compression = ZipUtil.zip(dir);
        String compression = "E:\\Primeton_DSP\\governor\\apache-tomcat-8.5.51\\webapps\\dsp\\WEB-INF\\lib\\com.primeton.dsp.upgrader-7.0.0-GA.jar";

        //解压缩
        ZipUtil.unZip(compression, out);

    }
}
