package com.gonghf.file.compression.zip;

public class ZipRun {
    public static void main(String[] args) {
        String dir = "F:/迅雷下载/moban4277/moban4277/examples/demo2";
        String out = "F:/test/zip";
        try {
            //压缩
            String compression = ZipUtil.zip(dir);

            //解压缩
            ZipUtil.unZip(compression,out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
