package com.gonghf.file.compression.tar;

public class TarRun {
    public static void main(String[] args) {
        String dir = "F:/迅雷下载/moban4277/moban4277/examples/demo2";
        String out = "F:/test/tar";
        try {
            //压缩
            String compression = TarUtil.tar(dir);

            //解压缩
            TarUtil.unTar(compression, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
